package ml.qingsu.greenrunner;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

/**
 * Created by w568w on 17-6-18.
 */
public class TutorialWizard extends BasicWizardLayout {

    /**
     * Note that initially BasicWizardLayout inherits from {@link android.support.v4.app.Fragment} and therefore you must have an empty constructor
     */
    public TutorialWizard() {
        super();
    }
    @Override
    public WizardFlow onSetup() {

        setNextButtonText("Next");
        setBackButtonText("Prev");
        setFinishButtonText("Finish");
        return new WizardFlow.Builder()
                .addStep(Step1.class)           //Add your steps in the order you want them
                .addStep(Step2.class,true)
                .addStep(Step3.class, true)
                .addStep(Step4.class)//to appear and eventually call create()
                .create();                              //to create the wizard flow.
    }
}