package com.asestefan.mutationcraft.registry;

import com.asestefan.mutationcraft.ModUtil;
import com.asestefan.mutationcraft.MutationcraftMod;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class ModRegistry<T> {
    public static final List<ModRegistry<?>> ALL = new ArrayList<>();

    private final ResourceKey<? extends Registry<T>> key;
    private final List<Entry<? extends T>> entries = new ArrayList<>();

    private ModRegistry(ResourceKey<? extends Registry<T>> key) {
        this.key = key;
        ALL.add(this);
    }

    public static <T> ModRegistry<T> create(ResourceKey<? extends Registry<T>> key) {
        return new ModRegistry<>(key);
    }

    public ResourceKey<? extends Registry<T>> key() {
        return key;
    }

    public List<Entry<? extends T>> entries() {
        return entries;
    }

    public <R extends T> Entry<R> register(String name, Supplier<R> factory) {
        Entry<R> entry = new Entry<>(this, ModUtil.id(MutationcraftMod.MODID, name), factory);
        entries.add(entry);
        return entry;
    }

    public void registerAll(BiConsumer<ResourceLocation, Supplier<T>> sink) {
        for (Entry<? extends T> entry : entries) {
            sink.accept(entry.getId(), entry::create);
        }
    }

    public static final class Entry<T> implements Supplier<T> {
        private final ModRegistry<?> owner;
        private final ResourceLocation id;
        private final Supplier<T> factory;
        private T value;

        private Entry(ModRegistry<?> owner, ResourceLocation id, Supplier<T> factory) {
            this.owner = owner;
            this.id = id;
            this.factory = factory;
        }

        private T create() {
            value = factory.get();
            return value;
        }

        public ResourceLocation getId() {
            return id;
        }

        @Override
        public T get() {
            return Objects.requireNonNull(value, () -> "Registry entry not present: " + id);
        }

        @SuppressWarnings("unchecked")
        public Holder<T> holder() {
            Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(owner.key.location());
            return registry.wrapAsHolder(get());
        }

        //? if >=1.21 {
        /*public Holder<T> ref() {
            return holder();
        }
        *///?} else {
        public T ref() {
            return get();
        }
        //?}
    }
}
