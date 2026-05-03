package kr.kro.teamdodoco.creatework;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CreateworkLang
{
    public static MutableComponent translateDirect(String key, Object... args)
    {
        return Component.translatable(Creatework.ID + "." + key, LangBuilder.resolveBuilders(args));
    }

    public static LangBuilder builder()
    {
        return new LangBuilder(Creatework.ID);
    }

    public static LangBuilder translate(String langKey, Object... args)
    {
        return builder().translate(langKey, args);
    }
}