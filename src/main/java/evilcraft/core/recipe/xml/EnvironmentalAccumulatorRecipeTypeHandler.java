package evilcraft.core.recipe.xml;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import evilcraft.core.weather.WeatherType;

/**
 * Handler for blood infuser recipes.
 * @author rubensworks
 *
 */
public class EnvironmentalAccumulatorRecipeTypeHandler extends SuperRecipeTypeHandler {

	@Override
	protected void handleRecipe(Element input, Element output, Element properties)
			throws XmlRecipeException {
		Node inputItem = input.getElementsByTagName("item").item(0);
		String inputWeather = input.getElementsByTagName("weather").item(0).getTextContent();
		Node outputItem = output.getElementsByTagName("item").item(0);
		String outputWeather = output.getElementsByTagName("weather").item(0).getTextContent();
		
		int duration = -1;
		int cooldowntime = -1;
		double processingspeed = -1.0D;
		if(properties.getElementsByTagName("duration").getLength() > 0) {
			duration = Integer.parseInt(properties.getElementsByTagName("duration").item(0).getTextContent());
		}
		if(properties.getElementsByTagName("cooldowntime").getLength() > 0) {
			cooldowntime = Integer.parseInt(properties.getElementsByTagName("cooldowntime").item(0).getTextContent());
		}
		if(properties.getElementsByTagName("processingspeed").getLength() > 0) {
			processingspeed = Double.parseDouble(properties.getElementsByTagName("processingspeed").item(0).getTextContent());
		}
		
		EnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                new EnvironmentalAccumulatorRecipeComponent(
                        (ItemStack) getItem(inputItem),
                        getWeatherType(inputWeather)
                ),
                new EnvironmentalAccumulatorRecipeComponent(
                		(ItemStack) getItem(outputItem),
                        getWeatherType(outputWeather)
                ),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldowntime, processingspeed)
        );
	}
	
	private WeatherType getWeatherType(String type) throws XmlRecipeException {
		WeatherType weather = WeatherType.valueOf(type);
		if(type == null) {
			throw new XmlRecipeException(String.format("Could not found the weather '%s'", type));
		}
		return weather;
	}

}
