package org.disco.bdd.prototype

import org.disco.easyb.plugin.EasybPlugin

class PrototypeLifeCycleAdapter implements EasybPlugin {
    public String getName() {
        return "prototype";
    }

    public void beforeScenario(Binding binding) {
        binding._foo_ = "test"
    }

    public void afterScenario(Binding binding) {

    }
}