package evilcraft.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Give this annotation to fields in {@link PacketCodec} to auto encode/decode them.
 * @author rubensworks
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface CodecField {

}
