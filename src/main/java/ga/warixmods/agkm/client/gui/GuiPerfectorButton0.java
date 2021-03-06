package ga.warixmods.agkm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPerfectorButton0 extends GuiButton {
	protected static final ResourceLocation buttonTextures1 = new ResourceLocation("agkm:textures/gui/perfectorGUI.png");

	public GuiPerfectorButton0(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y,40,40, buttonText);
		// TODO Auto-generated constructor stub
	}
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
    	
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(buttonTextures1);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = 20;

            if (flag)
            {
                k = 60;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k,106, this.width,this.height);
        }
    }
}
