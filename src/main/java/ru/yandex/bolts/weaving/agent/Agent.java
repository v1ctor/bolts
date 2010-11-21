package ru.yandex.bolts.weaving.agent;

import java.lang.instrument.Instrumentation;

import ru.yandex.bolts.weaving.LambdaClassFileTransformer;


/**
 * Weaving agent.
 *
 * @author Stepan Koltsov
 */
public class Agent {
    /*
    public void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new LambdaLoader());
    }
    */

    public void agentmain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new LambdaClassFileTransformer());
    }

} //~
