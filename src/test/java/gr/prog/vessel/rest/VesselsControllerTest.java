package gr.prog.vessel.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import gr.prog.vessel.GrVesselApplication;
import gr.prog.vessel.dto.*;
import gr.prog.vessel.repository.VisitRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrVesselApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class})
@DatabaseSetup("/sampleData.xml")
@ActiveProfiles("Test")
public class VesselsControllerTest {

	@Autowired
	private VisitRepository visitRepository;
	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	public void setContext(WebApplicationContext context) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getVisitors_success() throws Exception {
		Object result = visitRepository.getArrivalStatistic(2,
				Timestamp.valueOf(LocalDateTime.now().minusMonths(1)),
				Timestamp.valueOf(LocalDateTime.now().plusMonths(1)));
		System.out.println(result);

		// Port 2
		String responseJson = mockMvc.perform(get("/rest/port/{portId}/guests", 2)
				.param("t", "2015-01-02 07:07:00"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		List<GuestDto> response = objectMapper.readValue(responseJson, new TypeReference<List<GuestDto>>() {
		});
		Assert.assertEquals(1, response.size());
		Assert.assertEquals(11111L, (long) response.get(0).getImo());
		// Port 1
		responseJson = mockMvc.perform(get("/rest/port/{portId}/guests", 1)
				.param("t", "2015-01-02 07:07:00"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		response = objectMapper.readValue(responseJson, new TypeReference<List<GuestDto>>() {
		});
		Assert.assertEquals(1, response.size());
		Assert.assertEquals(55555, (long) response.get(0).getImo());
	}

	@Test
	public void getPortAggregation_stream_success() throws Exception {
		getPortAggregation_success("STREAM");
	}

	@Test
	public void getPortAggregation_jpql_success() throws Exception {
		getPortAggregation_success("JPQL");
	}

	// AVG() and COUNT() in native SQL return different types with different DB
	@Ignore
	public void getPortAggregation_sql_success() throws Exception {
		getPortAggregation_success("SQL");
	}

	public void getPortAggregation_success(String jpaMethod) throws Exception {
		String responseJson = mockMvc.perform(get("/rest/port/{portId}/aggregation", 2)
				.param("s", "2014-12-01 07:07:00")
				.param("e", "2015-02-01 07:07:00")
				.param("jpaMethod", jpaMethod))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		PortAggregationDto portAggregation = objectMapper.readValue(responseJson, PortAggregationDto.class);
		Assert.assertEquals(4L, (long) portAggregation.getUniqueVessels());
		Assert.assertEquals(3600L, (long) portAggregation.getMinInPortSec());
		Assert.assertEquals(259200L, (long) portAggregation.getMaxInPortSec());
		Assert.assertEquals(121680.0, portAggregation.getAvgInPortSec(), 0);
	}

	@Test
	public void getVesselAggregation_stream_success() throws Exception {
		getVesselAggregation_success("STREAM");
	}

	@Test
	public void getVesselAggregation_jpql_success() throws Exception {
		getVesselAggregation_success("JPQL");
	}

	// AVG() and COUNT() in native SQL return different types with different DB
	@Ignore
	public void getVesselAggregation_sql_success() throws Exception {
		getVesselAggregation_success("SQL");
	}

	public void getVesselAggregation_success(String jpaMethod) throws Exception {
		String responseJson = mockMvc.perform(get("/rest/port/{portId}/vessel/{imo}/aggregation", 2, 11111)
				.param("s", "2014-12-01 07:07:00")
				.param("e", "2015-02-01 07:07:00")
				.param("jpaMethod", jpaMethod))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		VesselAggregationDto vesselAggregationDto = objectMapper.readValue(responseJson, VesselAggregationDto.class);
		Assert.assertEquals(2L, (long) vesselAggregationDto.getPortVisits());
		Assert.assertEquals(172800, (long) vesselAggregationDto.getMinInPortSec());
		Assert.assertEquals(259200, (long) vesselAggregationDto.getMaxInPortSec());
		Assert.assertEquals(216000, vesselAggregationDto.getAvgInPortSec(), 0);
	}

	@Test
	public void getMonthAggregation_success() throws Exception {
		String responseJson = mockMvc.perform(get("/rest/port/{portId}/monthAggregation", 2)
				.param("y", "2015")
				.param("m", "1")
				.param("jpaMethod", JpaMethod.JPQL.name()))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		MonthlyAggregationDto monthlyAggregationDto = objectMapper.readValue(responseJson, MonthlyAggregationDto.class);
		Assert.assertEquals(4L, (long) monthlyAggregationDto.getTotalArrivals());
		Assert.assertEquals(4L, (long) monthlyAggregationDto.getUniqueVessels());
		Assert.assertEquals(87300, monthlyAggregationDto.getAvgDurationSec(), 0);
		Assert.assertEquals(1210.4, monthlyAggregationDto.getSumOfLength(), 0);
	}
}