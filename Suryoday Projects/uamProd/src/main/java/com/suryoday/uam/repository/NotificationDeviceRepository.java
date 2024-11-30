package com.suryoday.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.suryoday.uam.pojo.NotificationDevice;

public interface NotificationDeviceRepository extends JpaRepository<NotificationDevice, Long> {

	@Query(value = "select device_id from tbl_notificatiion_devices where device_id =:deviceId", nativeQuery = true)
	public String findByDeviceId(@Param("deviceId") String deviceId);

	public NotificationDevice findByUserId(String userId);

}
