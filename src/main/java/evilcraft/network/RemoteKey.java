package evilcraft.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.settings.KeyBinding;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

/**
 * Stores the state of a key pressed client-side which is then sent to the server.
 * 
 * Based on an elegant way to handle custom hotkeys server-side by
 * Nico Bergemann (BarracudaATA) in his "Dragon mounts" mod 
 * (http://www.minecraftforum.net/topic/827863-16x-dragon-mounts-r33-wip/)
 * 
 * @author immortaleeb
 *
 */
public class RemoteKey {
	/**
	 *  Name of the key
	 */
	public String name;
	/**
	 *  True if the key is pressed, false otherwise
	 */
	public boolean pressed;
	
	/**
	 * Make a new instance.
	 * @param kb The keybinding for this key.
	 */
	public RemoteKey(KeyBinding kb) {
		this(kb.keyDescription, kb.pressed);
	}
	
	/**
     * Make a new instance.
	 * @param name The name of this key.
	 * @param pressed If this key is pressed.
     */
	public RemoteKey(String name, boolean pressed) {
		this.name = name;
		this.pressed = pressed;
	}
	
	/**
	 * Convert this key to a byte array.
	 * @return The byte array.
	 */
	public byte[] toByteArray() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		
		byte[] res = null;
		
		try {
			dataStream.writeUTF(name);
			dataStream.writeBoolean(pressed);
			
			res = byteStream.toByteArray();
		} catch (IOException e) {
			// Shouldn't happen
			e.printStackTrace();
		} finally {
			try {
				byteStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				dataStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * Get a remote key from a byte array.
	 * @param data The byte array that contains an encoded byte array/
	 * @return The remote key.
	 */
	public static RemoteKey fromByteArray(byte[] data) {
		ByteArrayDataInput reader = ByteStreams.newDataInput(data);
		RemoteKey res = null;
		
		try {
			res = new RemoteKey(reader.readUTF(), reader.readBoolean());
		} catch (IllegalStateException e) {
			// Shouldn't happen...
			e.printStackTrace();
		}
		
		return res;
	}
}
