package ga.warixmods.agkm.entity.AI.copy;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class AttackOnCollidex extends EntityAIBase{
	 World worldObj;
	    protected EntityCreature attacker;
	    /** An amount of decrementing ticks that allows the entity to attack once the tick reaches 0. */
	    int attackTick;
	    /** The speed with which the mob will approach the target */
	    double speedTowardsTarget;
	    /** When true, the mob will continue chasing its target, even if it can't find a path to them right now. */
	    boolean longMemory;
	    /** The PathEntity of our entity. */
	    PathEntity entityPathEntity;
	    Class classTarget;
	    private int delayCounter;
	    private double targetX;
	    private double targetY;
	    private double targetZ;
	    private static final String __OBFID = "CL_00001595";
	    private int failedPathFindingPenalty = 0;
	    private boolean canPenalize = false;

	    public AttackOnCollidex(EntityCreature creature, Class targetClass, double speedIn, boolean useLongMemory)
	    {
	        this(creature, speedIn, useLongMemory);
	        this.classTarget = targetClass;
	        canPenalize = classTarget == null || !net.minecraft.entity.player.EntityPlayer.class.isAssignableFrom(classTarget); //Only enable delaying when not targeting players.
	    }

	    public AttackOnCollidex(EntityCreature creature, double speedIn, boolean useLongMemory)
	    {
	        this.attacker = creature;
	        this.worldObj = creature.worldObj;
	        this.speedTowardsTarget = speedIn;
	        this.longMemory = useLongMemory;
	        this.setMutexBits(3);
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	    	EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
	    	if (entitylivingbase == null)
	        {
	    		
	            return false;
	        }
	    	else if (!entitylivingbase.isEntityAlive())
	        {
	    		
	            return false;
	        }
	    	
	       return true;
	    }

	    /**
	     * Returns whether an in-progress EntityAIBase should continue executing
	     */
	    public boolean continueExecuting()
	    {
	        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
	        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase))));
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	    	
	        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
	        this.delayCounter = 0;
	    }

	    /**
	     * Resets the task
	     */
	    public void resetTask()
	    {
	        this.attacker.getNavigator().clearPathEntity();
	    }

	    /**
	     * Updates the task
	     */
	    public void updateTask()
	    {
	        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
	        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
	        double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
	        double d1 = this.func_179512_a(entitylivingbase);
	        --this.delayCounter;

	        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F))
	        {
	            this.targetX = entitylivingbase.posX;
	            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
	            this.targetZ = entitylivingbase.posZ;
	            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);

	            if (this.canPenalize)
	            {
	                this.targetX += failedPathFindingPenalty;
	                if (this.attacker.getNavigator().getPath() != null)
	                {
	                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
	                    if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
	                        failedPathFindingPenalty = 0;
	                    else
	                        failedPathFindingPenalty += 10;
	                }
	                else
	                {
	                    failedPathFindingPenalty += 10;
	                }
	            }

	            if (d0 > 1024.0D)
	            {
	                this.delayCounter += 10;
	            }
	            else if (d0 > 256.0D)
	            {
	                this.delayCounter += 5;
	            }

	            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
	            {
	                this.delayCounter += 15;
	            }
	        }

	        this.attackTick = Math.max(this.attackTick - 1, 0);

	        if (d0 <= d1 && this.attackTick <= 0)
	        {
	            this.attackTick = 20;

	            if (this.attacker.getHeldItem() != null)
	            {
	                this.attacker.swingItem();
	            }

	            this.attacker.attackEntityAsMob(entitylivingbase);
	            entitylivingbase.attackEntityFrom(DamageSource.generic, 5);
	            
	        }
	    }

	    protected double func_179512_a(EntityLivingBase attackTarget)
	    {
	        return (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width);
	    }
}
