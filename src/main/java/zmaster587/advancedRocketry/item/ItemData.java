package zmaster587.advancedRocketry.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zmaster587.advancedRocketry.api.DataStorage;

import java.util.List;

public class ItemData extends Item {

	int maxData;

	public ItemData(Properties props) {
		super(props);
	}

	public int getMaxData(ItemStack stack) {
		return 1000;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return getData(stack) == 0 ? super.getItemStackLimit(stack) : 1;
	}
	
	public int getData(ItemStack stack) {
		return getDataStorage(stack).getData();
	}
	
	public DataStorage.DataType getDataType(ItemStack stack) {
		return getDataStorage(stack).getDataType();
	}
	
	public DataStorage getDataStorage(ItemStack item) {

		DataStorage data = new DataStorage();

		if(!item.hasTag()) {
			data.setMaxData(getMaxData(item));
			CompoundNBT nbt = new CompoundNBT();
			data.writeToNBT(nbt);
		}
		else
			data.readFromNBT(item.getTag());

		return data;
	}

	public int addData(ItemStack item, int amount, DataStorage.DataType dataType) {
		DataStorage data = getDataStorage(item);

		int amt = data.addData(amount, dataType, true);

		CompoundNBT nbt = new CompoundNBT();
		data.writeToNBT(nbt);
		item.setTag(nbt);

		return amt;
	}

	public int removeData(ItemStack item, int amount, DataStorage.DataType dataType) {
		DataStorage data = getDataStorage(item);

		int amt = data.removeData(amount, true);

		CompoundNBT nbt = new CompoundNBT();
		data.writeToNBT(nbt);
		item.setTag(nbt);

		return amt;
	}

	public void setData(ItemStack item, int amount, DataStorage.DataType dataType) {
		DataStorage data = getDataStorage(item);

		data.setData(amount, dataType);

		CompoundNBT nbt = new CompoundNBT();
		data.writeToNBT(nbt);
		item.setTag(nbt);
	}

	@Override
	@OnlyIn(value=Dist.CLIENT)
	public void addInformation(ItemStack stack, World player,
			List list, ITooltipFlag bool) {
		super.addInformation(stack, player, list, bool);

		DataStorage data = getDataStorage(stack);

		list.add(new StringTextComponent(data.getData() + " / " + data.getMaxData() + " Data"));
		list.add(new StringTextComponent(I18n.format(data.getDataType().toString(), new Object[0])));

	}

}
