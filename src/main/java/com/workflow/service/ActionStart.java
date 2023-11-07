package com.workflow.service;

import com.workflow.entity.Rule;
import com.workflow.entity.Ticket;
import com.workflow.entity.actionConditionType.DateAction;
import com.workflow.entity.actionConditionType.StageAction;
import com.workflow.entity.actionConditionType.NumberAction;
import com.workflow.entity.actionConditionType.StringAction;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class  ActionStart {

    private static final Logger logger = LoggerFactory.getLogger(ActionStart.class);

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private StageService stageService;

    public void startAction(Rule rule, Ticket ticket, Long projectId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("action");
        switch (rule.getAction().getConditionType()){
            case NUMBER:
                actionOnNumber(rule,(NumberAction) rule.getAction(),ticket,projectId);
                break;
            case STRING:
                actionOnString(rule,(StringAction) rule.getAction(),ticket,projectId);
                break;
            case STAGE:
                break;
            case PROJECT:
                break;
            case DATE:
                actionOnDate(rule, (DateAction) rule.getAction(),ticket,projectId);
                break;
            default:
        }
    }

    public void actionOnNumber(Rule rule,NumberAction numberAction, Ticket ticket, Long projectId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(rule.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(rule.getActionField().getName()))) {

                    if (numberAction.getOperation() == null || numberAction == null)
                        continue;

                    logger.info(numberAction.getOperation() + "---less");
                    if (numberAction.getOperation().equals("set")) {
                        logger.info("set string action started");
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, numberAction.getNumber());
                    } else if (numberAction.getOperation().equals("remove")) {
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, null);
                    }
                }
            }
        }
    }

    public void actionOnDate(Rule rule, DateAction dateAction, Ticket ticket, Long projectId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(rule.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(rule.getActionField().getName()))) {

                    if (dateAction.getOperation() == null || dateAction == null)
                        continue;

                    logger.info(dateAction.getOperation() + "---less");
                    if (dateAction.getOperation().equals("set")) {
                        logger.info("set string action started");
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, dateAction.getDate());
                    } else if (dateAction.getOperation().equals("remove")) {
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, null);
                    }
                }
            }
        }
    }

    public void actionOnStageId(Rule rule, StageAction stageAction, Ticket ticket, Long projectId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + rule.getActionField().getName());
                if (attributeName.equals(rule.getActionField().getName())) {
                    if (stageAction.getOperation() == null || stageAction == null)
                        continue;
                    logger.info(stageAction.getOperation() + "---less");
                    if (stageAction.getOperation().equals("set")) {
                        logger.info("set Id action started");
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);

//                        Method setterMethod = ticketClass.getMethod(setterMethodName, Stage.class);
//                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        Long newId= stageAction.getNewId();
                        System.out.println(newId);
//                        setterMethod.invoke(ticket,newId );
                    } else if (stageAction.getOperation().equals("remove")) {
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
//                        setterMethod.invoke(ticket, null);
                    }
                }
            }
        }
    }

    public void actionOnString(Rule rule, StringAction stringAction, Ticket ticket, Long projectId)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("action on String ");
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName + "--" + capitalizeFirstLetter(rule.getActionField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(rule.getActionField().getName()))) {
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
                    } else if (stringAction.getOperation().equals("remove")) {
                        String setterMethodName = "set" + capitalizeFirstLetter(attributeName);
                        logger.info(setterMethodName);
                        Method setterMethod = ticketClass.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(ticket, null);
                    }
                }
            }
        }
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
