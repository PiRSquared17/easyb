package org.disco.easyb.listener

import org.disco.easyb.listener.DefaultListener
import org.disco.easyb.result.Result
import org.disco.easyb.util.BehaviorStepType

description "this is a story for working on the behavior step class along with the listener stuff"

scenario "listner should expose previous step", {
	
	given "a listener instance", {
		listener = new DefaultListener()
	}
	
	when "an event is recorded and stop has been called", {
		listener.startStep(BehaviorStepType.THEN, "success then step")
	    listener.gotResult(new Result(Result.SUCCEEDED))
	    listener.stopStep()
	}
	
	then "the listener should be able to indicate the last step was a THEN", {
		listener.getCurrentStep().name.shouldBe "easyb-genesis"
		and
		listener.getPreviousStep().stepType.type().shouldBe "then"
	}
	
}

scenario "step with a failed then should report failed scenario", {
  given "a listener instance", {
    listener = new DefaultListener()
  }

  when "scenario is recorded with a failed then inside", {
    listener.startStep(BehaviorStepType.SCENARIO, "failure then scenario")
      listener.startStep(BehaviorStepType.THEN, "failure then step")
      listener.gotResult(new Result(Result.FAILED))
      listener.stopStep()
    listener.stopStep()
  }

  then "genesis step should report a 1 failed scenario", {
    listener.genesisStep.failedScenarioCountForChildrenRecursively.shouldBe(1)
  }

}

scenario "step with no failed or pending then should report success scenario", {
  given "a listener instance", {
    listener = new DefaultListener()
  }

  when "scenario is recorded with a suceeded then inside", {
    listener.startStep(BehaviorStepType.SCENARIO, "success then scenario")
      listener.startStep(BehaviorStepType.THEN, "success then step")
      listener.gotResult(new Result(Result.SUCCEEDED))
      listener.stopStep()
    listener.stopStep()
  }

  then "genesis step should report a 0 failed scenario", {
    listener.genesisStep.failedScenarioCountForChildrenRecursively.shouldBe(0)
  }

}

scenario "step with a pending then should report pending scenario", {
  given "a listener instance", {
    listener = new DefaultListener()
  }

  when "scenario is recorded with a suceeded then inside", {
    listener.startStep(BehaviorStepType.SCENARIO, "pending then scenario")
      listener.startStep(BehaviorStepType.THEN, "pending then step")
      listener.gotResult(new Result(Result.PENDING))
      listener.stopStep()
    listener.stopStep()
  }

  then "genesis step should report 1 pending scenario", {
    listener.genesisStep.pendingScenarioCountForChildrenRecursively.shouldBe(1)
  }

}

scenario "step with a pending and a failed then should report 1 failed scenario and 0 pending scenario", {
  given "a listener instance", {
    listener = new DefaultListener()
  }

  when "scenario is recorded with 1 pending and 1 failed then inside", {
    listener.startStep(BehaviorStepType.SCENARIO, "failed then scenario")
      listener.startStep(BehaviorStepType.THEN, "pending then step")
      listener.gotResult(new Result(Result.PENDING))
      listener.stopStep()
      listener.startStep(BehaviorStepType.THEN, "failed then step")
      listener.gotResult(new Result(Result.FAILED))
      listener.stopStep()
    listener.stopStep()
  }

  then "genesis step should report 1 failed scenario", {
    listener.genesisStep.failedScenarioCountForChildrenRecursively.shouldBe(1)
  }

  then "genesis step should report 0 pending scenario", {
    listener.genesisStep.pendingScenarioCountForChildrenRecursively.shouldBe(0)
  }

}