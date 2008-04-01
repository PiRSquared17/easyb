package org.disco.easyb.listener

import org.disco.easyb.result.Result
import org.disco.easyb.util.BehaviorStepType
import org.disco.easyb.BehaviorStep

interface BehaviorListener {

  BehaviorStep startStep(BehaviorStepType specificationStepType, String stepName)
  void stopStep()

  BehaviorStep getGenesisStep()
  BehaviorStep getCurrentStep()
  BehaviorStep getPreviousStep()
  
  long getPendingSpecificationCount()
  long getFailedSpecificationCount()
  long getSuccessSpecificationCount()
  long getSpecificationCount()

  long getScenarioCount()
  long getSuccessScenarioCount()
  long getFailedScenarioCount()
  long getPendingScenarioCount()

  long getBehaviorCount()
  long getSuccessBehaviorCount()
  long getFailedBehaviorCount()
  long getPendingBehaviorCount()


  //probably expose a method here to get the executionLength
  void gotResult(result)
}