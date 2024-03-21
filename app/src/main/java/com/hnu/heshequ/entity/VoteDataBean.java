package com.hnu.heshequ.entity;

import java.util.List;



public class VoteDataBean {


    /**
     * isVote : 0
     * questions : [{"id":27,"gmtCreate":1530949617000,"voteId":17,"content":"111","category":1,"options":[{"id":83,"gmtCreate":1530949617000,"questionId":27,"content":"222","voteAmount":0},{"id":84,"gmtCreate":1530949617000,"questionId":27,"content":"333","voteAmount":0},{"id":85,"gmtCreate":1530949617000,"questionId":27,"content":"444","voteAmount":0}]}]
     */

    private int isVote;
    private List<QuestionsBean> questions;

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsBean> questions) {
        this.questions = questions;
    }

    public static class QuestionsBean {
        /**
         * id : 27
         * gmtCreate : 1530949617000
         * voteId : 17
         * content : 111
         * category : 1
         * options : [{"id":83,"gmtCreate":1530949617000,"questionId":27,"content":"222","voteAmount":0},{"id":84,"gmtCreate":1530949617000,"questionId":27,"content":"333","voteAmount":0},{"id":85,"gmtCreate":1530949617000,"questionId":27,"content":"444","voteAmount":0}]
         */

        private int id;
        private long gmtCreate;
        private int voteId;
        private String content;
        private int category;
        private List<OptionsBean> options;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public List<OptionsBean> getOptions() {
            return options;
        }

        public void setOptions(List<OptionsBean> options) {
            this.options = options;
        }

        public static class OptionsBean {
            /**
             * id : 83
             * gmtCreate : 1530949617000
             * questionId : 27
             * content : 222
             * voteAmount : 0
             */

            private int id;
            private long gmtCreate;
            private int questionId;
            private String content;
            private int voteAmount;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getGmtCreate() {
                return gmtCreate;
            }

            public void setGmtCreate(long gmtCreate) {
                this.gmtCreate = gmtCreate;
            }

            public int getQuestionId() {
                return questionId;
            }

            public void setQuestionId(int questionId) {
                this.questionId = questionId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getVoteAmount() {
                return voteAmount;
            }

            public void setVoteAmount(int voteAmount) {
                this.voteAmount = voteAmount;
            }
        }
    }
}
