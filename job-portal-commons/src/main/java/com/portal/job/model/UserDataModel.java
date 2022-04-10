package com.portal.job.model;

import java.util.HashSet;
import java.util.Set;

public class UserDataModel {

    private final UserBasicDataModel userBasicDataModel;
    private final Set<UserEducationDataModel> userEducationDataModels;
    private final Set<UserExperienceDataModel> userExperienceDataModels;
    private final Set<UserCertificationDataModel> userCertificationDataModels;
    private final Set<UserProjectDataModel> userProjectDataModels;
    private final Set<UserPatentDataModel> userPatentDataModels;
    private final Set<UserPublicationDataModel> userPublicationDataModels;
    private final Set<UserVolunteerDataModel> userVolunteerDataModels;
    private final Set<UserTestDataModel> userTestDataModels;

    public static class Builder {

        private UserBasicDataModel userBasicDataModel;
        private Set<UserEducationDataModel> userEducationDataModels;
        private Set<UserExperienceDataModel> userExperienceDataModels;
        private Set<UserCertificationDataModel> userCertificationDataModels;
        private Set<UserProjectDataModel> userProjectDataModels;
        private Set<UserPatentDataModel> userPatentDataModels;
        private Set<UserPublicationDataModel> userPublicationDataModels;
        private Set<UserVolunteerDataModel> userVolunteerDataModels;
        private Set<UserTestDataModel> userTestDataModels;

        public Builder(final UserBasicDataModel userBasicDataModel) {
            this.userBasicDataModel = userBasicDataModel;
        }

        public Builder userEducationDataModels(final Set<UserEducationDataModel> userEducationDataModels) {
            this.userEducationDataModels = userEducationDataModels;
            return this;
        }

        public Builder userEducationDataModel(final UserEducationDataModel userEducationDataModel) {
            if (null == this.userEducationDataModels) {
                this.userEducationDataModels = new HashSet<UserEducationDataModel>();
            }
            this.userEducationDataModels.add(userEducationDataModel);
            return this;
        }

        public Builder userExperienceDataModels(final Set<UserExperienceDataModel> userExperienceDataModels) {
            this.userExperienceDataModels = userExperienceDataModels;
            return this;
        }

        public Builder userExperienceDataModel(final UserExperienceDataModel userExperienceDataModel) {
            if (null == this.userExperienceDataModels) {
                this.userExperienceDataModels = new HashSet<UserExperienceDataModel>();
            }
            this.userExperienceDataModels.add(userExperienceDataModel);
            return this;
        }

        public Builder userProjectDataModels(final Set<UserProjectDataModel> userProjectDataModels) {
            this.userProjectDataModels = userProjectDataModels;
            return this;
        }

        public Builder userProjectDataModel(final UserProjectDataModel userProjectDataModel) {
            if (null == this.userProjectDataModels) {
                this.userProjectDataModels = new HashSet<UserProjectDataModel>();
            }
            this.userProjectDataModels.add(userProjectDataModel);
            return this;
        }

        public Builder userCertificationDataModels(final Set<UserCertificationDataModel> userCertificationDataModels) {
            this.userCertificationDataModels = userCertificationDataModels;
            return this;
        }

        public Builder userCertificationDataModel(final UserCertificationDataModel userCertificationDataModel) {
            if (null == this.userCertificationDataModels) {
                this.userCertificationDataModels = new HashSet<UserCertificationDataModel>();
            }
            this.userCertificationDataModels.add(userCertificationDataModel);
            return this;
        }

        public Builder userPatentDataModels(final Set<UserPatentDataModel> userPatentDataModels) {
            this.userPatentDataModels = userPatentDataModels;
            return this;
        }

        public Builder userPatentDataModel(final UserPatentDataModel userPatentDataModel) {
            if (null == this.userPatentDataModels) {
                this.userPatentDataModels = new HashSet<UserPatentDataModel>();
            }
            this.userPatentDataModels.add(userPatentDataModel);
            return this;
        }

        public Builder userPublicationDataModels(final Set<UserPublicationDataModel> userPublicationDataModels) {
            this.userPublicationDataModels = userPublicationDataModels;
            return this;
        }

        public Builder userPublicationDataModel(final UserPublicationDataModel userPublicationDataModel) {
            if (null == this.userPublicationDataModels) {
                this.userPublicationDataModels = new HashSet<UserPublicationDataModel>();
            }
            this.userPublicationDataModels.add(userPublicationDataModel);
            return this;
        }

        public Builder userVolunteerDataModels(final Set<UserVolunteerDataModel> userVolunteerDataModels) {
            this.userVolunteerDataModels = userVolunteerDataModels;
            return this;
        }

        public Builder userVolunteerDataModel(final UserVolunteerDataModel userVolunteerDataModel) {
            if (null == this.userVolunteerDataModels) {
                this.userVolunteerDataModels = new HashSet<UserVolunteerDataModel>();
            }
            this.userVolunteerDataModels.add(userVolunteerDataModel);
            return this;
        }

        public Builder userTestDataModels(final Set<UserTestDataModel> userTestDataModels) {
            this.userTestDataModels = userTestDataModels;
            return this;
        }

        public Builder userTestDataModel(final UserTestDataModel userTestDataModel) {
            if (null == this.userTestDataModels) {
                this.userTestDataModels = new HashSet<UserTestDataModel>();
            }
            this.userTestDataModels.add(userTestDataModel);
            return this;
        }

        public UserDataModel build() {
            return new UserDataModel(this);
        }

    }

    public UserDataModel(final Builder builder) {
        this.userBasicDataModel = builder.userBasicDataModel;
        this.userCertificationDataModels = builder.userCertificationDataModels;
        this.userEducationDataModels = builder.userEducationDataModels;
        this.userExperienceDataModels = builder.userExperienceDataModels;
        this.userPatentDataModels = builder.userPatentDataModels;
        this.userProjectDataModels = builder.userProjectDataModels;
        this.userPublicationDataModels = builder.userPublicationDataModels;
        this.userTestDataModels = builder.userTestDataModels;
        this.userVolunteerDataModels = builder.userVolunteerDataModels;
    }

    public UserBasicDataModel getUserBasicDataModel() {
        return userBasicDataModel;
    }

    public Set<UserEducationDataModel> getUserEducationDataModels() {
        return userEducationDataModels;
    }

    public Set<UserExperienceDataModel> getUserExperienceDataModels() {
        return userExperienceDataModels;
    }

    public Set<UserCertificationDataModel> getUserCertificationDataModels() {
        return userCertificationDataModels;
    }

    public Set<UserProjectDataModel> getUserProjectDataModels() {
        return userProjectDataModels;
    }

    public Set<UserPatentDataModel> getUserPatentDataModels() {
        return userPatentDataModels;
    }

    public Set<UserPublicationDataModel> getUserPublicationDataModels() {
        return userPublicationDataModels;
    }

    public Set<UserVolunteerDataModel> getUserVolunteerDataModels() {
        return userVolunteerDataModels;
    }

    public Set<UserTestDataModel> getUserTestDataModels() {
        return userTestDataModels;
    }

    @Override
    public String toString() {
        return "UserDataModel [userBasicDataModel=" + userBasicDataModel + ", userEducationDataModels="
                + userEducationDataModels + ", userExperienceDataModels=" + userExperienceDataModels
                + ", userCertificationDataModels=" + userCertificationDataModels + ", userProjectDataModels="
                + userProjectDataModels + ", userPatentDataModels=" + userPatentDataModels
                + ", userPublicationDataModels=" + userPublicationDataModels + ", userVolunteerDataModels="
                + userVolunteerDataModels + ", userTestDataModels=" + userTestDataModels + "]";
    }

}
