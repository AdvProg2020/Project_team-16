package ModelPackage.System;

import java.util.ArrayList;

public class ManagerManager {
    private static ManagerManager managerManager = null;
    private ManagerManager(){ }
    public static ManagerManager getInstance(){
        if (managerManager == null)
            managerManager = new ManagerManager();
        return managerManager;
    }

}
