package br.com.voluntplus.volunteerservices.application.service;

import br.com.voluntplus.users.api.CurrentRole;
import br.com.voluntplus.users.api.UserSummary;
import br.com.voluntplus.users.api.UsersApi;
import br.com.voluntplus.volunteerservices.application.port.in.ListOwnedVolunteerServicesUseCase;
import br.com.voluntplus.volunteerservices.application.port.in.OwnedVolunteerServiceResult;
import br.com.voluntplus.volunteerservices.application.port.out.VolunteerServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListOwnedVolunteerServicesService implements ListOwnedVolunteerServicesUseCase {

	private final UsersApi usersApi;
	private final VolunteerServiceRepository repository;

	public ListOwnedVolunteerServicesService(
			UsersApi usersApi,
			VolunteerServiceRepository repository) {
		this.usersApi = usersApi;
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OwnedVolunteerServiceResult> listOwnedServices() {
		UserSummary owner = usersApi.requireCurrentUserWithRole(CurrentRole.OFFERER);
		return repository.findAllByOwnerIdAndNotDeleted(owner.userId()).stream()
				.map(service -> new OwnedVolunteerServiceResult(service, owner))
				.toList();
	}
}
