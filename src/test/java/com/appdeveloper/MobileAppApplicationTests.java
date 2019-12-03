package com.appdeveloper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MobileAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	
	
	@Test
	public void getElement26() throws Exception {

		this.mockMvc.perform(get("/data/25")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.time_period").value("2006")).andExpect(jsonPath("$.ref_area").value("EL"))
				.andExpect(jsonPath("$.indicator").value("eun_web"))
				.andExpect(jsonPath("$.breakdown").value("grade11voc"))
				.andExpect(jsonPath("$.unit_measure").value("pc_schools"))
				.andExpect(jsonPath("$.value").value("0.638"));
	}

	@Test
	public void getMin() throws Exception {
		this.mockMvc.perform(get("/min")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Min"))
				.andExpect(jsonPath("$.description").value("Valore minimo per pc_schools"))
				.andExpect(jsonPath("$.value").value("0.206"));
	}

}
