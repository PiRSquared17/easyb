package org.disco.easyb

import org.disco.easyb.listener.ExecutionListener

class StoryBinding extends Binding {
    StoryKeywords story

    def StoryBinding(ExecutionListener listener) {
        this.story = new StoryKeywords(listener)

        before = {description = "", closure = {} ->
            story.before(description, closure)
        }

        assumption = before.curry("story assumption")
        assuming = before.curry("story assumes")

        after = {description = "", closure = {} ->
            story.after(description, closure)
        }

        before_each = {description = "", closure = {} ->
            story.beforeEach(description, closure)
        }

        after_each = {description = "", closure = {} ->
            story.afterEach(description, closure)
        }

        scenario = {description, closure = story.pendingClosure ->
            story.scenario(description, closure)
        }

        then = {spec, closure = story.pendingClosure ->
            story.then(spec, closure)
        }

        when = {description, closure = {} ->
            story.when(description, closure)
        }

        given = {description, closure = {} ->
            story.given(description, closure)
        }

        and = {description = "", closure = {} ->
            story.and(description, closure)
        }

        narrative = {description = "", closure = {} ->
            story.narrative(description, closure)
        }

        description = {description ->
            story.description description
        }

        easybResults = {
            story.easybResults()
        }

        all = {
            story.all()
        }

        ignore = {Object ... scenarios ->
            if (scenarios.size() == 1) {
                def objscn = scenarios[0]
                try {
                    objscn.call()
                } catch (excep) {
                    if (scenarios[0].getClass() == String) {
                        story.ignore([scenarios[0]])
                    } else {
                        story.ignore(scenarios[0])
                    }
                }
            } else if (scenarios.size() > 1) {
                story.ignore(scenarios as List)
            } else {
                //no argument but this isn't working
            }
        }
    }

    def getAt(ArrayList list) {
        this.story.ignoreList = list
    }

    /**
     * This method returns a fully initialized Binding object (or context) that
     * has definitions for methods such as "when" and "given", which are used
     * in the context of stories.
     */
    static Binding getBinding(listener) {
        return new StoryBinding(listener)
    }
}
