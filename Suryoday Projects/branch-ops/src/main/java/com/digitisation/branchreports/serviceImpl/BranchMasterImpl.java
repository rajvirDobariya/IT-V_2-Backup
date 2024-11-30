package com.digitisation.branchreports.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.model.BranchMaster;
import com.digitisation.branchreports.repository.BranchMasterRepo;
import com.digitisation.branchreports.service.BranchMasterService;
import com.digitisation.branchreports.utils.EXCELService;

@Service
public class BranchMasterImpl implements BranchMasterService {

	@Autowired
	public BranchMasterRepo branchmasterrepo;

	@Override
	public List<BranchMaster> getAll() {
		return branchmasterrepo.findAll();
	}

	public List<Long> testingBranches = new ArrayList<>(new HashSet<>(Arrays.asList(10079L, 10015L, 10020L, 10163L,
			10012L, 10077L, 10170L, 10166L, 50065L, 50807L, 50048L, 10701L, 50209L, 50320L, 50193L, 50172L, 50212L,
			10198L, 10161L, 10164L, 10169L, 10021L, 10025L, 50285L, 50234L, 50300L, 10249L, 10022L, 10108L, 10078L,
			50156L, 50031L, 10023L, 50094L, 50104L, 50297L, 50283L, 50287L, 10038L, 10070L, 10081L, 10171L, 10236L,
			10167L, 10231L, 10013L, 10080L, 10080L, 10173L, 10107L, 10197L, 10234L, 10235L, 10160L, 10158L, 10011L,
			10011L, 10162L, 10090L, 10014L, 10241L, 50080L, 10012L, 10091L, 10069L, 10253L, 10250L, 10250L, 10105L,
			10095L, 10026L, 50189L, 10096L, 10105L, 10096L, 50048L, 50172L, 10097L, 10088L, 10092L, 10089L, 10106L,
			10106L, 10233L, 10233L, 10236L, 10231L, 10241L, 10243L, 10075L, 10078L, 10244L, 10169L, 10021L, 10025L,
			50285L, 50234L, 50300L, 10249L, 10022L, 10108L, 50156L, 50031L, 10023L, 50094L, 50104L, 10020L, 50297L,
			50287L, 10038L, 10070L, 10081L, 50283L, 10074L, 10165L, 10246L, 10245L, 10239L, 50349L, 50249L, 10086L,
			10019L, 50804L, 10236L, 10234L, 10241L, 10233L, 50075L, 50165L, 50123L, 50049L, 10168L, 50999L, 10242L,
			50091L, 50158L, 50166L, 10024L, 50073L, 50116L, 10246L, 10245L, 10076L, 10243L, 10250L, 10244L, 50640L,
			50036L, 50056L, 10249L, 10234L, 10233L, 10236L, 10231L, 10241L, 10244L, 10017L, 10094L, 10093L, 50079L,
			10018L, 10083L, 10084L, 10085L, 10235L, 10246L, 10239L, 10250L, 10245L, 10082L, 10234L, 10233L, 10236L,
			10231L, 10241L, 10244L, 10017L, 10094L, 10093L, 50079L, 10018L, 10083L, 10235L, 10239L, 10250L, 10245L,
			10082L, 10016L, 10020L, 10163L, 10232L, 10702L, 50041L, 50044L, 10246L, 10028L, 10159L, 50640L, 10084L,
			10085L,10254L,10087L,10248L)));


	@Override
	public List<BranchMaster> getBranchById(String requestString) {
		JSONObject requestJSON = new JSONObject(requestString);
		Long branchId = requestJSON.optLong("branchId");
		if (branchId == null || branchId == 0) {
			throw new DigitizationException("Please enter branchId!");
		}
		return branchmasterrepo.findByBranches(Arrays.asList(branchId));
	}

	@Override
	public List<Long> getTestingBranches() {
		testingBranches.forEach(t -> System.out.print(", " + t));
		return testingBranches;
	}

	@Override
	public String updateTestingBranches(String requestString) {
		JSONObject requestJSON = new JSONObject(requestString);
		JSONArray jsonArray = requestJSON.getJSONArray("longList");

		if (jsonArray == null || jsonArray.length() == 0) {
			throw new DigitizationException("Please enter branchIds!");
		}

		// Process
		List<Long> branchIds = IntStream.range(0, jsonArray.length()).mapToObj(jsonArray::getLong)
				.collect(Collectors.toList());
		testingBranches = branchIds;
		return "Update Testing Branches Successfully!";
	}

	@Override
	public void addRemainingBranches() {
		List<BranchMaster> savedBranches = branchmasterrepo.findAll();
		List<Long> savedBranchIds = savedBranches.stream().map(BranchMaster::getBranchid).collect(Collectors.toList());
		List<BranchMaster> excelBranches = EXCELService.getBranchesFromExcel();
		System.out.println("SAVED :" + savedBranches.size());
		System.out.println("EXCEL :" + excelBranches.size());
		int count = 0;
		for (BranchMaster excelBranch : excelBranches) {
			if (!savedBranchIds.contains(excelBranch.getBranchid())) {
				excelBranch.setCreatedDate(LocalDateTime.now());
				branchmasterrepo.save(excelBranch);
				count++;
			}

		}
		System.out.println("COUNT :" + count);
	}

	@Override
	public String getTestingBranchesCount() {
		return "Testing Branches Count is ::" + testingBranches.size();
	}
}
