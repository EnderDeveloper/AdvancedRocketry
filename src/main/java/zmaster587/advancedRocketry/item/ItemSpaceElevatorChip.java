package zmaster587.advancedRocketry.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import zmaster587.advancedRocketry.util.DimensionBlockPosition;
import zmaster587.advancedRocketry.util.NBTStorableListList;
import zmaster587.libVulpes.LibVulpes;

import java.util.List;

public class ItemSpaceElevatorChip extends Item {

	
	public ItemSpaceElevatorChip(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	public List<DimensionBlockPosition> getBlockPositions(ItemStack stack) {
		NBTStorableListList list = new NBTStorableListList();
		
		if(stack.hasTag()) {
				list.readFromNBT(stack.getTag());
		}
		
		return list.getList();
	}
	
	public void setBlockPositions(ItemStack stack, List<DimensionBlockPosition> listToStore) {
		NBTStorableListList list = new NBTStorableListList(listToStore);
		
		if(stack.hasTag()) {
			
			if(listToStore.isEmpty())
				stack.getTag().remove("positions");
			else {
				list.writeToNBT(stack.getTag());
			}
		}
		else if(!listToStore.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			list.writeToNBT(nbt);
			
			stack.setTag(nbt);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World player,
			List list, ITooltipFlag bool) {
		
		int numPos = getBlockPositions(stack).size();
		
		if(numPos > 0)
			list.add(new StringTextComponent("Contains " + numPos + " entries"));
		else
			list.add(new TranslationTextComponent("msg.empty"));
	}
	
}
