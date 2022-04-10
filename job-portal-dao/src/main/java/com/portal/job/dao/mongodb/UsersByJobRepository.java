package com.portal.job.dao.mongodb;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portal.job.dao.mongodb.model.UserByJob;

@Repository
public interface UsersByJobRepository extends
		MongoRepository<UserByJob, Serializable> {

	public List<UserByJob> findByJob_JobId(final Long jobId,
			final Pageable pageable);

	public List<UserByJob> findByJob_JobId(final Long jobId, final Sort sort);

	public Long countByJob_JobId(final Long jobId);


	public List<UserByJob> findByUser_UserId(final Long userId,
			final Pageable pageable);

	public List<UserByJob> findByUser_UserId(final Long userId, final Sort sort);

	public Long countByUser_UserId(final Long userId);

	public UserByJob findByUser_UserIdAndJob_JobId(final Long userId,
			final Long jobId);

	public UserByJob findOneByUser_UserIdAndJob_JobId(final Long userId,
			final Long jobId);

	public List<UserByJob> findByJob_JobIdAndUser_UserIdNotIn(final Long jobId,
			final Set<Long> userIds, final Pageable pageable);

	public Long countByJob_JobIdAndUser_UserIdNotIn(final Long jobId,
			final Set<Long> userIds);
}
