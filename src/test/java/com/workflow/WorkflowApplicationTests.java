package com.workflow;

import com.workflow.entity.Field;
import com.workflow.entity.Project;
import com.workflow.entity.Rule;
import com.workflow.repository.RuleRepo;
import com.workflow.service.RuleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class WorkflowApplicationTests {

	@Test
	void contextLoads() {
	}

//	@Mock
//	private RuleRepo ruleRepo;
//
//	@InjectMocks
//	private RuleService ruleService;
//
//	@Test
//	void testFindByTriggerFieldAndProjectId() {
//		// Create test data
//		Field triggerField = new Field();  // Replace with actual initialization
//		String projectId = "testProjectId";
//
//		Rule rule1 = new Rule();
//		rule1.setRuleId(1L);
//		rule1.setProject(new Project());  // Replace with actual initialization
//
//		Rule rule2 = new Rule();
//		rule2.setRuleId(2L);
//		rule2.setProject(new Project());  // Replace with actual initialization
//
//		List<Rule> expectedRules = Arrays.asList(rule1, rule2);
//
//		// Mock the repository method
//		when(ruleRepo.findByTriggerFieldAndProjectId(triggerField, projectId)).thenReturn(expectedRules);
//
//		// Call the repository method
//		List<Rule> actualRules = ruleRepo.findByTriggerFieldAndProjectId(triggerField, projectId);
//
//		// Verify the result
//		assertEquals(expectedRules, actualRules);
//	}

}
