/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [24/01/2016, 17:56:30 (GMT)]
 */
package vazkii.psi.common.spell.operator.vector;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRaycastAxis extends PieceOperator {

	SpellParam origin;
	SpellParam ray;
	SpellParam max;

	public PieceOperatorVectorRaycastAxis(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(ray = new ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false));
		addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 originVal = this.<Vector3>getParamValue(context, origin);
		Vector3 rayVal = this.<Vector3>getParamValue(context, ray);

		if(originVal == null || rayVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		double maxLen = SpellContext.MAX_DISTANCE;
		Double numberVal = this.<Double>getParamValue(context, max);
		if(numberVal != null)
			maxLen = numberVal.doubleValue();
		maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

		Vector3 end = originVal.copy().add(rayVal.copy().normalize().multiply(maxLen));

		MovingObjectPosition pos = context.caster.worldObj.rayTraceBlocks(originVal.toVec3D(), end.toVec3D());
		if(pos == null || pos.getBlockPos() == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		EnumFacing facing = pos.sideHit;
		return new Vector3(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
