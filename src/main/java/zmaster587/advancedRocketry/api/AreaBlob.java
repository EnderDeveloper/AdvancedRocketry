package zmaster587.advancedRocketry.api;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import zmaster587.advancedRocketry.api.util.IBlobHandler;
import zmaster587.libVulpes.util.AdjacencyGraph;
import zmaster587.libVulpes.util.HashedBlockPosition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AreaBlob {
	//Graph containing the acutal area enclosed
	protected AdjacencyGraph<HashedBlockPosition> graph;
	//Object to call back to when events happen, usually a tileentity
	protected IBlobHandler blobHandler;
	//Data stored by this blob
	Object data;

	public AreaBlob(IBlobHandler blobHandler) {
		this.blobHandler = blobHandler;
		graph = new AdjacencyGraph<HashedBlockPosition>();
		data = null;
	}

	public void setData(Object obj) {
		data = obj;
	}
	
	public boolean isPositionAllowed(World world, HashedBlockPosition pos, List<AreaBlob> otherBlobs) {
		return true;
	}
	
	public Object getData() {
		return data;
	}
	
	public int getBlobMaxRadius() {
		return blobHandler.getMaxBlobRadius();
	}
	
	/**
	 * Adds a block position to the blob
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addBlock(int x, int y , int z, List<AreaBlob> otherBlobs) {
		HashedBlockPosition blockPos = new HashedBlockPosition(x, y, z);
		addBlock(blockPos, otherBlobs);
	}
	
	/**
	 * Adds a block to the graph
	 * @param blockPos block to add
	 */
	public void addBlock(HashedBlockPosition blockPos, List<AreaBlob> otherBlobs) {
		if(!graph.contains(blockPos) && blobHandler.canFormBlob()) {
			graph.add(blockPos, getPositionsToAdd(blockPos));
		}
	}
	
	/**
	 * @return the BlockPosition of the root of the blob
	 */
	public HashedBlockPosition getRootPosition() {
		return blobHandler.getRootPosition();
	}
	
	/**
	 * Gets adjacent blocks if they exist in the blob
	 * @param blockPos block to find things adjacent to
	 * @return list containing valid adjacent blocks
	 */
	protected HashSet<HashedBlockPosition> getPositionsToAdd(HashedBlockPosition blockPos) {
		HashSet<HashedBlockPosition> set = new HashSet<HashedBlockPosition>();
		
		for(Direction direction : Direction.values()) {
			
			HashedBlockPosition offset = blockPos.getPositionAtOffset(direction);
			if(graph.contains(offset))
				set.add(offset);
		}
		
		return set;
	}

	/**
	 * Given a block position returns whether or not it exists in the graph
	 * @return true if the block exists in the blob
	 */
	public boolean contains(HashedBlockPosition position) {
		boolean contains = false;
		
		//synchronized (graph) {
			contains = graph.contains(position);
		//}
		return contains;
	}
	
	/**
	 * Given a block position returns whether or not it exists in the graph
	 * @param x
	 * @param y
	 * @param z
	 * @return true if the block exists in the blob
	 */
	public boolean contains(int x, int y, int z) {
		return contains(new HashedBlockPosition(x, y, z));
	}

	/**
	 * Called when this blob is about to overlap another blob
	 * @param otherBlob other blob about to be overlapped
	 * @return true if this blob is allowed to overlap the otherBlob
	 */
	public boolean canBlobsOverlap(int x, int y, int z, AreaBlob otherBlob) {
		return blobHandler.canBlobsOverlap(new HashedBlockPosition(x, y, z), otherBlob);
	}

	/**
	 * Removes the block at the given coords for this blob
	 * @param x
	 * @param y
	 * @param z
	 */
	public void removeBlock(HashedBlockPosition blockPos) {
		//HashedBlockPosition blockPos = new HashedBlockPosition(x, y, z);
		graph.remove(blockPos);
		
		for(Direction direction : Direction.values()) {

			HashedBlockPosition newBlock = blockPos.getPositionAtOffset(direction);
			if(graph.contains(newBlock) && !graph.doesPathExist(newBlock, blobHandler.getRootPosition()))
				graph.removeAllNodesConnectedTo(newBlock);
		}
		
	}
	
	/**
	 * Removes all nodes from the blob
	 */
	public void clearBlob() {
		graph.clear();
	}
	
	/**
	 * @return a set containing all locations
	 */
	public Set<HashedBlockPosition> getLocations() {
		return graph.getKeys();
	}
	
	/**
	 * @return the number of elements in the blob
	 */
	public int getBlobSize() {
		return graph.size();
	}
}
