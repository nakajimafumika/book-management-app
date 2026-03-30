package manpowergroup.jp.bookSystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserve_info")
public class ReserveInfo {
	@Id
	@Column(name = "management_id")
	private String managementId;

	@Column(name = "reserves_date")
	private LocalDate reservesDate;

	@Column(name = "reserve_user")
	private String reserveUser;

	// Getters and Setters
	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public LocalDate getReservesDate() {
		return reservesDate;
	}

	public void setReservesDate(LocalDate reservesDate) {
		this.reservesDate = reservesDate;
	}

	public String getReserveUser() {
		return reserveUser;
	}

	public void setReserveUser(String reserveUser) {
		this.reserveUser = reserveUser;
	}

}
