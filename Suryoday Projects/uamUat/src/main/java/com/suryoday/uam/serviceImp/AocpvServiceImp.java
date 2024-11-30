package com.suryoday.uam.serviceImp;

import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.PageGroup;
import com.suryoday.uam.pojo.PermissionDto;
import com.suryoday.uam.repository.PageGroupRepository;
import com.suryoday.uam.service.AocpService;

@Service
public class AocpvServiceImp implements AocpService {

	@Autowired
	PageGroupRepository pageGroupRepository;

	@Override
	public List<PermissionDto> getPagesForOtherRole(String otherRole, String CHANNEL, String userAccess,
			List<PermissionDto> dtos, String moduleName) {
		JSONArray array = new JSONArray(otherRole);

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject jsonObject = array.getJSONObject(i);
			String role = jsonObject.getString("role");
//			Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);
			Optional<PageGroup> findById2 = pageGroupRepository.findIdByPageName(role);

			if (findById2.isPresent()) {

				PageGroup pageGroup = findById2.get();
				Boolean validate = (moduleName == null ? pageGroup.getChannel().equals(CHANNEL)
						: pageGroup.getChannel().equals(CHANNEL)
								&& pageGroup.getModuleGroup().getModelGroupName().equalsIgnoreCase(moduleName));
				if (validate) {
					PermissionDto dto = new PermissionDto();
					dto.setIndex(pageGroup.getId());
					dto.setName(pageGroup.getModuleGroup().getModelGroupName());
					dto.setPage(pageGroup.getPageName());
					dto.setTitle(pageGroup.getPageName());
					dto.setPage(pageGroup.getPage());
					dto.setChannel(pageGroup.getChannel());
					if (userAccess != null) {
						org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
						int count = 0;
						for (int n = 0; n < userAccessInJson.length(); n++) {
							String asset = userAccessInJson.getString(n);
							if (asset.equalsIgnoreCase(dto.getName())) {
								dtos.add(dto);
							} else if (dto.getName().equals("COMMON")) {
								if (count == 0) {
									dtos.add(dto);
								}
								count++;
							}
						}
					}
				}
			}
		}
		return dtos;
	}

}
