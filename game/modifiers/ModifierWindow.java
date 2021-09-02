package game.modifiers;

import java.awt.Frame;

public abstract class ModifierWindow extends Frame {

	private static final long serialVersionUID = 1L;

	public abstract void setModifier(Modifier modifier);
}
