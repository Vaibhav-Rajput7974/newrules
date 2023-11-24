package com.workflow.service;

import com.workflow.dto.ResponseWebSocket;
import com.workflow.entity.*;
import com.workflow.entity.actionConditionType.*;
import com.workflow.entity.triggerConditionTypes.ConditionOnTrigger;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.StageRepo;
import com.workflow.repository.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.workflow.entity.actionConditionType.ConditionOnAction.NUMBER;

@Service
public class  ActionStart {

    private static final Logger logger = LoggerFactory.getLogger(ActionStart.class);

    @Autowired
    private  SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ResponseWebSocket responseWebSocket;

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private StageService stageService;
    @Autowired
    private StageRepo stageRepo;

    public void startAction(List<Action> actionList, Ticket ticket) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for(Action action : actionList){
            switch (action.getActionCondition().getConditionType()){
                case NUMBER:
                    actionOnNumber(action,ticket);
                    break;
                case STRING:
                    actionOnString(action,ticket);
                    break;
                case STAGE:
                    actionOnStage(action,ticket);
                    break;
                case PROJECT:
                    actionProject(action,ticket);
                    break;
                case DATE:
                    actionOnDate(action,ticket);
                    break;
                case USER:
                    actionOnUser(action,ticket);
                default:
            }
        }
        logger.info("action");
    }

    public void actionOnNumber(Action action, Ticket ticket) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        NumberAction numberAction = (NumberAction) action.getActionCondition();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(action.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getName()))) {

                    if (numberAction.getOperation() == null || numberAction == null)
                        continue;

                    logger.info(numberAction.getOperation() + "---less");
                    if (numberAction.getOperation().equals("set")) {
                        logger.info("set string action started");
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, Long.class);
                        setterMethod.invoke(ticket, numberAction.getNumber());
//                        Ticket savedTicket = ticketRepo.save(ticket);
////                        if(rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
//                            responseWebSocket.sendResponse(savedTicket);
////                        }
                    }
                }
            }
        }
    }

    public void actionOnDate(Action action, Ticket ticket) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        DateAction dateAction = (DateAction) action.getActionCondition();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(action.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getName()))) {

                    if (dateAction.getOperation() == null || dateAction == null)
                        continue;

                    logger.info(dateAction.getOperation() + "---less");
                    if (dateAction.getOperation().equals("set")) {
                        logger.info("set date action started");
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, Date.class);
                        setterMethod.invoke(ticket, dateAction.getDate());
////                        if(rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
//                            Ticket savedTicket = ticketRepo.save(ticket);
//                            responseWebSocket.sendResponse(savedTicket);
////                        }
                    }
                }
            }
        }
    }

    public void actionProject(Action action, Ticket ticket){
        ProjectAction projectAction = (ProjectAction) action.getActionCondition();
        logger.info("project set startes");
        String operation=projectAction.getOperation();
        Long currentprojectId=projectAction.getProjectId();
        Long stageId= projectAction.getStageId();
        Optional<Project> projectOptional = projectRepo.findById(currentprojectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            Optional<Stage> stageOptional = project.getStageList().stream()
                    .filter(stage -> stage.getStageId().equals(stageId))
                    .findFirst();
            if (stageOptional.isPresent()) {
                Stage stage = stageOptional.get();
                if(operation.equals("set")){
                    logger.info("stage Set Successfully");
                    logger.info(String.valueOf(stage.getStageId()));
                    ticket.setStage(stage);
                    ticketRepo.save(ticket);
                }
            }
        }
    }

    public void actionOnStage(Action action, Ticket ticket) {
        logger.info("Changing stage to: {}");
        StageAction stageAction = (StageAction) action.getActionCondition();
        try {
            System.out.println("runing");
            Stage stage = stageRepo.findById(stageAction.getNewId()).get();
            if(stageAction.getOperation().equals("set")){
                logger.info("set Stage");
                ticket.setStage(stage);
////                if(rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
//                    Ticket newTicket =ticketRepo.save(ticket);
//                    responseWebSocket.sendResponse(newTicket);
////                }
            }

        }catch (NumberFormatException e){
            logger.info("stage Id not found");
        }
        catch (Exception e) {
            logger.error("Error occurred while saving ticket: {}", e.getMessage(), e);
        }
    }


    public void actionOnString(Action action, Ticket ticket)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        StringAction stringAction = (StringAction) action.getActionCondition();

        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(action.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getName()))) {
                    if (stringAction.getOperation() == null || stringAction == null)
                        continue;
                    logger.info(stringAction.getOperation() + "---less");
                    if (stringAction.getOperation().equals("set")) {
                        logger.info("set string action started");
                        logger.info(stringAction.getNextString());
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, stringAction.getNextString());
////                        if(rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
//                            Ticket savedTicket = ticketRepo.save(ticket);
//                            responseWebSocket.sendResponse(savedTicket);
////                        }
                    }
                }
            }
        }
    }

    public void actionOnUser(Action action, Ticket ticket)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("action on User ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        UserAction userAction = (UserAction) action.getActionCondition();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(action.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getName()))) {
                    if (userAction.getOperation() == null || userAction == null)
                        continue;
                    logger.info(userAction.getOperation() + "---less");
                    if (userAction.getOperation().equals("set")) {
                        logger.info("set string action started");
                        logger.info(String.valueOf(userAction.getUserAction()));
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, User.class);
                        setterMethod.invoke(ticket, userAction.getUserAction());
////                        if(rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
//                            Ticket savedTicket = ticketRepo.save(ticket);
//                            responseWebSocket.sendResponse(savedTicket);
////                        }
                    }
                }
            }
        }
    }
    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
