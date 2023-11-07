package com.workflow.service;


import com.workflow.entity.Project;
import com.workflow.entity.Rule;
import com.workflow.entity.Ticket;
import com.workflow.entity.triggerConditionTypes.*;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.StageRepo;
import com.workflow.repository.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TriggerStart {

    private static final Logger logger = LoggerFactory.getLogger(TriggerStart.class);
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private StageService stageService;
    @Autowired
    private ActionStart actionStart;

    @Autowired
    private StageRepo stageRepo;



    public void updateTicketTrigger(Ticket existing,Ticket updated,Long projectId){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent()) {
            logger.info("status_trigger");
            List<Rule> ruleList = optionalProject.get().getRulesList();
            ruleList.forEach((rule) -> {
                logger.info(rule.getTriggerField().getDataType()+"NUMBER");
                if(rule.getTriggerField().getDataType().equals("NUMBER")){
                    NumberTrigger numberTrigger = (NumberTrigger) rule.getTrigger();
                    try {
                        triggerOnNumber(numberTrigger,existing,updated,rule,projectId);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                } else if (rule.getTriggerField().getDataType().equals("STRING")) {
                    StringTrigger stringTrigger = (StringTrigger) rule.getTrigger();
                    try {
                        triggerOnString(stringTrigger,existing,updated,rule,projectId);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                } else if (rule.getTriggerField().getDataType().equals("DATE")) {
                    DateTrigger dateTrigger = (DateTrigger) rule.getTrigger();
                    try {
                        triggerOnDate(updated,dateTrigger,rule,projectId);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }



    public void triggerOnNumber(NumberTrigger numberTrigger,Ticket existing,Ticket updated,Rule rule,Long projectId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);

                if (attributeName.equals(capitalizeFirstLetter(rule.getTriggerField().getName()))) {

                    if(numberTrigger.getOperation() == null || numberTrigger == null)
                        continue;

                    Long existingValue = null;
                    if(existing != null){
                        existingValue=(Long) method.invoke(existing);
                    }

                    Long updatedValue =null;
                    if(updated != null){
                        updatedValue = (Long) method.invoke(updated);
                    }
                    logger.info(numberTrigger.getOperation() + "---less");
                    if (numberTrigger.getOperation().equals("less")) {
                        if(updatedValue < numberTrigger.getValue()){
                            logger.info("number set started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                        logger.info("set string  exicuted");
                    } else if (numberTrigger.getOperation().equals("equall")) {
                        if(updatedValue == numberTrigger.getValue()){
                            logger.info("string equall started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                    } else if (numberTrigger.getOperation().equals("greater")) {
                        if(existingValue > numberTrigger.getValue()){
                            logger.info("string change started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                    }
                }
            }
        }
    }

    public void triggerOnString(StringTrigger stringTrigger,Ticket existing,Ticket updated,Rule rule,Long projectId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName+"+++++++++_"+capitalizeFirstLetter(rule.getTriggerField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(rule.getTriggerField().getName()))) {

                    if(stringTrigger.getOperation() == null || stringTrigger == null)
                        continue;

                    String existingValue =null;
                    if(existing != null){
                        existingValue=(String) method.invoke(existing);
                    }

                    String updatedValue =null;
                    if(updated != null){
                        updatedValue = (String) method.invoke(updated);
                    }

                    logger.info(stringTrigger.getOperation() + "---less");
                    if (stringTrigger.getOperation().equals("set")) {
                        logger.info(updatedValue+"----"+stringTrigger.getCurrentString());
                        if(updatedValue.equals(stringTrigger.getCurrentString())){
                            logger.info("string set started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                        logger.info("set string  exicuted");
                    } else if (stringTrigger.getOperation().equals("change")) {
                        System.out.println(updatedValue+"new"+stringTrigger.getCurrentString());
                        System.out.println(existingValue+"old"+stringTrigger.getPreviousString());
                        if(updatedValue != null  && existingValue != null &&  updatedValue.equals(stringTrigger.getCurrentString()) && existingValue.equals(stringTrigger.getPreviousString())){
                            logger.info("string change started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                    } else if (stringTrigger.getOperation().equals("remove")) {
                        if(existingValue.equals(stringTrigger.getPreviousString())){
                            logger.info("string remove started");
                            actionStart.startAction(rule,updated,projectId);
                        }
                    }
                }
            }
        }
    }


    public void triggerOnDate(Ticket ticket,DateTrigger dateTrigger,Rule rule,Long projectId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Class<?> ticketClass = Ticket.class;
        Method[] methods = ticketClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String attributeName = method.getName().substring(3);
                logger.info(attributeName+"+++++++++_"+capitalizeFirstLetter(rule.getTriggerField().getName()));
                if (attributeName.equals(capitalizeFirstLetter(rule.getTriggerField().getName()))) {

                    if(dateTrigger.getOperation() == null || dateTrigger == null)
                        continue;

                    Date updatedValue =null;
                    if(ticket != null){
                        updatedValue = (Date) method.invoke(ticket);
                    }
                    logger.info(dateTrigger.getOperation() + "---less");
                    if (dateTrigger.getOperation().equals("before")) {
                        dueDateApprocha(-dateTrigger.getDays(),-dateTrigger.getDays(),-dateTrigger.getMinuter(),rule,projectId);
                        logger.info("before date  exicuted");
                    } else if (dateTrigger.getOperation().equals("equall")) {
                        logger.info("equall date exicuted");
                        dueDateApprocha(0,0,0,rule,projectId);
                    } else if (dateTrigger.getOperation().equals("after")) {
                        logger.info("after date exicuted");
                       dueDateApprocha(dateTrigger.getDays(),dateTrigger.getDays(),dateTrigger.getMinuter(),rule,projectId);
                    } else if (dateTrigger.getOperation().equals("set")) {
                        if(dateTrigger.getDate().compareTo(updatedValue) == 0) {
                            logger.info("set date exicute ");
                            actionStart.startAction(rule,ticket,projectId);
                        }
                    }
                }
            }
        }
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public void dueDateApprocha(int days,int hours,int minute,Rule rule,Long projectId){
        Project project = projectRepo.findById(projectId).orElse(null);

        if (project != null) {
            Date currentDate = new Date();
            List<Ticket> ticketList= project.getStageList().stream()
                    .flatMap(stage -> stage.getTicketList().stream())
                    .collect(Collectors.toList());
            ticketList.forEach(ticket -> {
                Calendar calendar = Calendar.getInstance();
                if (ticket.getTicketEndingDate() != null) {
                    calendar.setTime(ticket.getTicketEndingDate());
                    calendar.add(Calendar.DAY_OF_MONTH,days);
                    calendar.add(Calendar.HOUR,hours);
                    calendar.add(Calendar.MINUTE,minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND,0);
                    Date notificationDate = calendar.getTime();

                    Calendar currentCalendar = Calendar.getInstance();
                    currentCalendar.setTime(currentDate);
                    currentCalendar.set(Calendar.SECOND,0);
                    currentCalendar.set(Calendar.MILLISECOND, 0);
                    Date truncatedCurrentDate = currentCalendar.getTime();

                    System.out.println(notificationDate + "--" + truncatedCurrentDate);

                    if (notificationDate.compareTo(truncatedCurrentDate) == 0 ) { // Check if notificationDate is within a 10-second window
                        System.out.println("hello");
                        try {
                            actionStart.startAction(rule, ticket, projectId);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }


    public void stageTrigger(Long previousStage,Long currentStage,Ticket ticket,Long projectId){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent()) {
            List<Rule> ruleList = optionalProject.get().getRulesList();
            ruleList.forEach((rule) -> {
                if(rule.getTrigger().getConditionType() == ConditionOnTrigger.STAGE){
                    StageTrigger stageCondition= (StageTrigger) rule.getTrigger();
                    if(stageCondition.equals("set") ){
                        if(stageCondition.getCurrentStage().equals(currentStage)){
                            logger.info("stage set exicuted");
                        }
                    } else if (stageCondition.equals("change")) {
                        if(stageCondition.getPreviousStage().equals(previousStage) && stageCondition.getCurrentStage().equals(currentStage)){
                            logger.info("stage change exicuted");
                        }
                    } else if (stageCondition.equals("remove") ) {
                        if(stageCondition.getPreviousStage().equals(previousStage)){
                            logger.info("stage removed exicuted");
                        }
                    }
                }
            });
        }
    }
}
