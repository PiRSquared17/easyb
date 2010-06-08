package org.easyb;


import org.easyb.domain.Behavior;
import org.easyb.domain.Story;
import org.easyb.listener.ResultsCollector;
import org.easyb.result.Result;
import org.easyb.util.BehaviorStepType;

import java.util.Arrays;
import java.util.List;

/**
 * this class prints data to the console related to running
 * easyb stories and specifications. This is a user facing class and
 * thus, a lot of effort goes towards human readable messages that
 * convey precise information. note, there is a lot of conditional logic
 * to determine various result altering messages; that is, pending and
 * ignored behaviors change the behavior of reporting output.
 */
public class ConsoleReporter extends ResultsCollector {
  private long startTime;

  public void startBehavior(final Behavior behavior) {
    System.out.println("Running " + behavior.getPhrase() + " " + behavior.getTypeDescriptor()
                       + " (" + behavior.getFile().getName() + ")");
    startTime = System.currentTimeMillis();
  }

  public void stopBehavior(final BehaviorStep currentStep, final Behavior behavior) {
    final long endTime = System.currentTimeMillis();
    printMetrics(behavior, startTime, this.getPreviousStep(), endTime);
  }

  public void completeTesting() {
    if (getBehaviorCount() == 0) {
      System.out.println("No behaviors were run");
    } else {
      System.out.println("\n" +
                         this.getTotalRanCountMessage() +
                         this.getTotalPendingCountMessage() +
                         ( getFailedBehaviorCount() > 0 ? " with " +
                                                          ( getFailedBehaviorCount() == 1 ? "1 failure" : getFailedBehaviorCount() + " failures" ) : " with no failures" ) +
                         this.getCompletedIgnoredMesage());
    }
  }

  /**
   * this method returned a formatted string the total pending count and
   * some information regarding the ignored count
   * example strings:
   * 3 of 9 behaviors ran with no failures (6 behaviors were ignored)
   * 21 of 27 behaviors ran (including 9 pending behaviors and 6 ignored behaviors) with no failures
   */
  private String getTotalPendingCountMessage() {
    if (getPendingBehaviorCount() > 0) {
      String messge = " (including " +
                      ( getPendingBehaviorCount() == 1 ? "1 pending behavior" : getPendingBehaviorCount() + " pending behaviors" );
      if (getIgnoredScenarioCount() > 0) {
        messge +=
          ( getIgnoredScenarioCount() > 0 ? ""
                                            + ( getIgnoredScenarioCount() == 1 ? " with 1 behavior ignored)" : " and " + getIgnoredScenarioCount()
                                                                                                               + " ignored behaviors" ) : "" );
      }
      return messge + ")";
    } else {
      return "";
    }
  }

  /**
   * this method only returns the ignored count if there are NO pending messages
   * as the ignored count is included in the pending message to make the
   * output more human readable
   */
  private String getCompletedIgnoredMesage() {
    if (getPendingBehaviorCount() > 0) {
      return "";
    } else {
      return ( getIgnoredScenarioCount() > 0 ? " ("
                                               + ( getIgnoredScenarioCount() == 1 ? "1 behavior was ignored)" : getIgnoredScenarioCount() + " behaviors were ignored)" ) : "" );
    }
  }

  /**
   * this method formats the total run behaviors -- like with @getScenariosRunMessage,
   * this method calculates the total by subtracting any ignored scenarios
   */
  private String getTotalRanCountMessage() {
    if (getIgnoredScenarioCount() > 0) {
      return getBehaviorCount() + " of " + ( getBehaviorCount() + getIgnoredScenarioCount() ) + " behaviors ran";
    } else {
      return ( getBehaviorCount() > 1 ? getBehaviorCount() + " total behaviors ran" : "1 behavior ran" );
    }
  }

  /**
   * this method returns a formatted string that contains the total scenarios run.
   * if there were ignored scenarios, the total number is determined by subtracting
   * the ignored ones; thus, we don't convey that an ignored scenario was "run"
   */
  private String getScenariosRunMessage(final BehaviorStep step) {
    if (step.getIgnoredScenarioCountRecursively() > 0) {
      return "Scenarios run: (" +
             +( step.getScenarioCountRecursively() - step.getIgnoredScenarioCountRecursively() )
             + " of "
             + step.getScenarioCountRecursively()
             + ")";
    } else {
      return "Scenarios run: " + step.getScenarioCountRecursively();
    }
  }

  private String getStepRunMessage(final String name, final BehaviorStepType type, final BehaviorStep step) {
    long aftersTotal = step.getBehaviorCountRecursively(type, null);
    long afters = step.getBehaviorCountRecursively(type, Result.FAILED);

    return name + "'s run: " + aftersTotal + ", Failures: " + afters;
  }


  private void printMetrics(final Behavior behavior, final long startTime,
                            final BehaviorStep currentStep, final long endTime) {
    if (behavior instanceof Story) {
      System.out.println(( currentStep.getFailedScenarioCountRecursively() == 0 ? "" : "FAILURE " ) +
                         this.getScenariosRunMessage(currentStep) +
                         ", Failures: " + currentStep.getFailedScenarioCountRecursively() +
                         ", Pending: " + currentStep.getPendingScenarioCountRecursively() +
                         ( currentStep.getIgnoredScenarioCountRecursively() > 0 ? ", Ignored: " + currentStep.getIgnoredScenarioCountRecursively() : "" ) +
                         ", Time elapsed: " + ( endTime - startTime ) / 1000f + " sec\n" +
          getStepRunMessage("Before", BehaviorStepType.BEFORE, currentStep) + "\n" +
          getStepRunMessage("After", BehaviorStepType.AFTER, currentStep) + "\n" +
          getStepRunMessage("Before Scenario", BehaviorStepType.BEFORE_EACH, currentStep) + "\n" +
          getStepRunMessage("After Scenario", BehaviorStepType.AFTER_EACH, currentStep) + "\n"
      );
    } else {
      System.out.println(( currentStep.getFailedSpecificationCountRecursively() == 0 ? "" : "FAILURE " ) +
                         "Specifications run: " + currentStep.getSpecificationCountRecursively() +
                         ", Failures: " + currentStep.getFailedSpecificationCountRecursively() +
                         ", Pending: " + currentStep.getPendingSpecificationCountRecursively() +
                         ", Time elapsed: " + ( endTime - startTime ) / 1000f + " sec");
    }
    if ( currentStep.getBehaviorCountListRecursively(BehaviorStepType.grossCountableTypes, Result.FAILED) > 0 ) {
      handleFailurePrinting(currentStep);
    }
  }

  private static final List<BehaviorStepType> recurseErrorStepTypes = Arrays.asList(BehaviorStepType.SCENARIO,
               BehaviorStepType.GENESIS, BehaviorStepType.STORY, BehaviorStepType.SPECIFICATION, BehaviorStepType.EXECUTE,
               BehaviorStepType.AFTER, BehaviorStepType.AFTER_EACH, BehaviorStepType.BEFORE, BehaviorStepType.BEFORE_EACH
  );

  private void handleFailurePrinting(final BehaviorStep currentStep) {
    for (BehaviorStep step : currentStep.getChildSteps()) {
      if (recurseErrorStepTypes.contains(step.getStepType())) {
        handleFailurePrinting(step);
      } else {
        printFailureMessage(step);
      }
    }
  }

  private void printFailureMessage(final BehaviorStep istep) {

    if (istep.getResult() != null && istep.getResult().failed() ) {
      System.out.println("\t" + istep.getParentStep().getStepType().type() + " \"" + istep.getParentStep().getName() + "\"");
      System.out.println("\tstep " + istep.getStepType().toString() +  " \"" + istep.getName() + "\" -- " +
                    (istep.getResult().cause() != null ? istep.getResult().cause().getMessage() : istep.getResult().description) );
    }
  }
}