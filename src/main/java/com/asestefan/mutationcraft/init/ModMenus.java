package com.asestefan.mutationcraft.init;

import com.asestefan.mutationcraft.menu.FlamethrowerMenu;
import com.asestefan.mutationcraft.platform.Services;
import com.asestefan.mutationcraft.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    public static final ModRegistry<MenuType<?>> REGISTRY = ModRegistry.create(Registries.MENU);
    public static final ModRegistry.Entry<MenuType<FlamethrowerMenu>> FLAMETHROWER = REGISTRY.register("flamethrower",
            () -> Services.PLATFORM.createMenuType(FlamethrowerMenu::client));

    private ModMenus() {
    }
}
