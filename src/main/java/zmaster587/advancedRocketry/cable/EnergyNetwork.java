package zmaster587.advancedRocketry.cable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zmaster587.libVulpes.api.IUniversalEnergy;
import zmaster587.libVulpes.util.UniversalBattery;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

public class EnergyNetwork extends CableNetwork implements IUniversalEnergy {
	/**
	 * Create a new network and get an ID
	 * @return ID of this new network
	 */
	
	UniversalBattery battery;
	
	public EnergyNetwork() {
		battery = new UniversalBattery(500);
	}
	
	public static EnergyNetwork initNetwork() {
		Random random = new Random(System.currentTimeMillis());

		int id = random.nextInt();

		while(usedIds.contains(id)){ id = random.nextInt(); };

		EnergyNetwork net = new EnergyNetwork();
		usedIds.add(id);
		net.networkID = id;

		return net;
	}
	
	@Override
	public boolean merge(CableNetwork cableNetwork) {
		//Try not to lose power
		if(super.merge(cableNetwork)) {
			battery.acceptEnergy(((EnergyNetwork)cableNetwork).battery.getUniversalEnergyStored(), false);
			return true;
		}
		
		return false;
	}

	//TODO: balance tanks
	@Override
	public void tick() {
		int amount = 1000;
		//Return if there is nothing to do
		if(sinks.isEmpty() || (sources.isEmpty() && battery.getUniversalEnergyStored() == 0))
			return;



		//Go through all sinks, if one is not full attempt to fill it

		int demand = 0;
		int supply = battery.getUniversalEnergyStored();
		Iterator<Entry<TileEntity,Direction>> sinkItr = sinks.iterator();
		Iterator<Entry<TileEntity,Direction>> sourceItr = sources.iterator();

		while(sinkItr.hasNext()) {
			//Get tile and key
			Entry<TileEntity,Direction> obj = (Entry<TileEntity, Direction>)sinkItr.next();
			LazyOptional<IEnergyStorage> dataHandlerSink = obj.getKey().getCapability(CapabilityEnergy.ENERGY, obj.getValue());

			demand += dataHandlerSink.orElse(null).receiveEnergy(amount, true);
		}

		while(sourceItr.hasNext()) {
			//Get tile and key
			Entry<TileEntity,Direction> obj = (Entry<TileEntity, Direction>)sourceItr.next();
			LazyOptional<IEnergyStorage> dataHandlerSink = obj.getKey().getCapability(CapabilityEnergy.ENERGY, obj.getValue());

			supply += dataHandlerSink.orElse(null).extractEnergy(amount, true);
		}
		int amountMoved, amountToMove;
		amountMoved = amountToMove = Math.min(supply, demand);

		sinkItr = sinks.iterator();
		while(sinkItr.hasNext()) {


			//Get tile and key
			Entry<TileEntity,Direction> obj = (Entry<TileEntity, Direction>)sinkItr.next();
			LazyOptional<IEnergyStorage> dataHandlerSink = obj.getKey().getCapability(CapabilityEnergy.ENERGY, obj.getValue());


			amountToMove -= dataHandlerSink.orElse(null).receiveEnergy(amountToMove, false);
		}
		
		//Try to drain internal buffer first
		amountMoved -= battery.extractEnergy(amountMoved, false);

		sourceItr = sources.iterator();
		while(sourceItr.hasNext()) {
			//Get tile and key
			Entry<TileEntity,Direction> obj = (Entry<TileEntity, Direction>)sourceItr.next();
			LazyOptional<IEnergyStorage> dataHandlerSink = obj.getKey().getCapability(CapabilityEnergy.ENERGY, obj.getValue());

			amountMoved -= dataHandlerSink.orElse(null).extractEnergy(amountMoved, false);
		}
	}

	@Override
	public void setEnergyStored(int amt) {
		
	}

	@Override
	public int extractEnergy(int amt, boolean simulate) {
		return 0;
	}

	@Override
	public int getUniversalEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		return 0;
	}

	@Override
	public int acceptEnergy(int amt, boolean simulate) {
		return battery.acceptEnergy(amt, simulate);
	}

	@Override
	public void setMaxEnergyStored(int max) {
		
	}

	@Override
	public boolean canReceive() {
		return false;
	}

	@Override
	public boolean canExtract() {
		return false;
	}
}