package ace.actually.ccdrones.menu.slots;

import com.mojang.datafixers.util.Pair;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.impl.TurtleUpgrades;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LinkUpgradeSlot extends Slot {
    public LinkUpgradeSlot(Container container, int slot, int xPos, int yPos) {
        super(container, slot, xPos, yPos);
    }

    TagKey<Item> UPGRADE = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("cclink", "link_upgrades"));

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(UPGRADE);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
