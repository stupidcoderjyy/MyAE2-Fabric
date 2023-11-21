package stupidcoder.ae2.core;

import stupidcoder.ae2.core.registry.AEItems;
import stupidcoder.common.core.Mod;

public abstract class AE extends Mod {
    public static AE instance;

    public AE() {
        instance = this;
        if (isEnvDataGen) {
            return;
        }
        AEItems.register();
    }
}
