package org.polyscape.logic;

public record LogicContainer(Logic logic, LogicType logicType, int linkId, int logicId, String logicName) {
    public LogicContainer setLogicId(int logicId){
        return new LogicContainer(logic, logicType, linkId, logicId, logicName);
    }
}
