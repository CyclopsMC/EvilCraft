package evilcraft.network;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * Packet with automatic coding and decoding of basic fields annotated with {@link CodecField}.
 * @author rubensworks
 *
 */
public abstract class PacketCodec extends PacketBase {
	
	private static Map<Class<?>, ICodecAction> codecActions = Maps.newHashMap();
	static {
		codecActions.put(String.class, new ICodecAction() {

			@Override
			public void encode(Object object, ByteArrayDataOutput output) {
				output.writeUTF((String) object);
			}

			@Override
			public Object decode(ByteArrayDataInput input) {
				return input.readUTF();
			}
		});
		
		codecActions.put(double.class, new ICodecAction() {

			@Override
			public void encode(Object object, ByteArrayDataOutput output) {
				output.writeDouble((Double) object);
			}

			@Override
			public Object decode(ByteArrayDataInput input) {
				return input.readDouble();
			}
		});
		
		codecActions.put(int.class, new ICodecAction() {

			@Override
			public void encode(Object object, ByteArrayDataOutput output) {
				output.writeInt((Integer) object);
			}

			@Override
			public Object decode(ByteArrayDataInput input) {
				return input.readInt();
			}
		});
		
		codecActions.put(boolean.class, new ICodecAction() {

			@Override
			public void encode(Object object, ByteArrayDataOutput output) {
				output.writeBoolean((Boolean) object);
			}

			@Override
			public Object decode(ByteArrayDataInput input) {
				return input.readBoolean();
			}
		});
		
		codecActions.put(float.class, new ICodecAction() {

			@Override
			public void encode(Object object, ByteArrayDataOutput output) {
				output.writeFloat((Float) object);
			}

			@Override
			public Object decode(ByteArrayDataInput input) {
				return input.readFloat();
			}
		});
	}
	
	private void loopCodecFields(ICodecRunnable runnable) {
		Field[] fields = this.getClass().getDeclaredFields();
		
		// Sort this because the Java API tells us that getDeclaredFields()
		// does not deterministically define the order of the fields in the array.
		// Otherwise we might get nasty class cast exceptions when running in SMP.
		Arrays.sort(fields, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		
		for(final Field field : fields) {
			if(field.isAnnotationPresent(CodecField.class)) {
				Class<?> clazz = field.getType();
				ICodecAction action = codecActions.get(clazz);
				if(action == null) {
					System.err.println("No ICodecAction was found for " + clazz
							+ ". You should add one in PacketCodec.");
				}
				
				// Make private fields temporarily accessible.				
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				runnable.run(field, action);
				field.setAccessible(accessible);
			}
		}
	}

	@Override
	public void encode(final ByteArrayDataOutput output) {
		loopCodecFields(new ICodecRunnable() {
			
			@Override
			public void run(Field field, ICodecAction action) {
				Object object = null;
				try {
					object = field.get(PacketCodec.this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				action.encode(object, output);
			}
		});
	}

	@Override
    public void decode(final ByteArrayDataInput input) {
		loopCodecFields(new ICodecRunnable() {

			@Override
			public void run(Field field, ICodecAction action) {
				Object object = action.decode(input);
				try {
					field.set(PacketCodec.this, object);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	private interface ICodecAction {
		
		/**
		 * Encode the given object.
		 * @param object The object to encode into the output.
		 * @param output The byte array to encode to.
		 */
		public void encode(Object object, ByteArrayDataOutput output);

		/**
		 * Decode from the input.
		 * @param input The byte array to decode from.
		 * @return The object to return after reading it from the input.
		 */
	    public Object decode(ByteArrayDataInput input);
	    
	}
	
	private interface ICodecRunnable {
		
		/**
		 * Run a type of codec.
		 * @param field The field annotated with {@link CodecField}.
		 * @param action The action that must be applied to the field.
		 */
		public void run(Field field, ICodecAction action);
		
	}
	
}
