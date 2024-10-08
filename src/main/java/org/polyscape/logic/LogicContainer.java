package org.polyscape.logic;

import java.io.File;

public record LogicContainer(Logic logic, LogicType logicType, int linkId, int logicId, String logicName, File logicFile) {
    public LogicContainer setLogicId(int logicId){
        return new LogicContainer(logic, logicType, linkId, logicId, logicName, logicFile);
    }
}
