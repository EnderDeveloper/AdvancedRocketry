package zmaster587.advancedRocketry.integration.jei.crystallizer;

import mezz.jei.api.helpers.IJeiHelpers;
import zmaster587.libVulpes.interfaces.IRecipe;
import zmaster587.libVulpes.recipe.RecipesMachine;

import java.util.LinkedList;
import java.util.List;

public class CrystallizerRecipeMaker {

	public static List<CrystallizerWrapper> getMachineRecipes(IJeiHelpers helpers, Class clazz) {
		
		List<CrystallizerWrapper> list = new LinkedList<CrystallizerWrapper>();
		for(IRecipe rec : RecipesMachine.getInstance().getRecipes(clazz)) {
			list.add(new CrystallizerWrapper(rec));
		}
		return list;
	}
	
}
