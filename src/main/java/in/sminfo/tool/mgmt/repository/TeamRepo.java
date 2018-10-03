package in.sminfo.tool.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.Team;

public interface TeamRepo extends JpaRepository<Team, Integer> {

	Team findByTeamName(String teamName);

	List<Team> findByManagerUid(Integer managerUid);
}
