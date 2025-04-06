package io.github.yunivers.treegrowingsimulator.config;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config
{
    @ConfigRoot(value = "config", visibleName = "TreeGrowingSimulator2011 Config")
    public static final ConfigFields config = new ConfigFields();

    public static class ConfigFields
    {
        @ConfigEntry(
                name = "Wait Time",
                description = "The amount of ticks (times 5) you must be sprinting before bonemeal is applied",
                maxLength = Integer.MAX_VALUE)
        public Integer waitTime = 100;

        @ConfigEntry(
                name = "Growth Chance",
                description = "The chance (in %) for the crop to grow",
                maxLength = 100,
                minLength = 0)
        public Integer growthChance = 45;

        @ConfigEntry(
                name = "Ignore Crops",
                description = "Whether or not to ignore crops when twerking")
        public Boolean ignoreCrops = true;
    }
}
