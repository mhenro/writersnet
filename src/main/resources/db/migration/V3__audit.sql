-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE custom_audit_entity (
  id INTEGER NOT NULL,
  timestamp BIGINT NOT NULL,
  user_id varchar(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE public.billing_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  user_id varchar NOT NULL,
  operation_type integer,
  operation_cost bigint NOT NULL,
  balance bigint NOT NULL,
  operation_date timestamp,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE billing_aud
  ADD CONSTRAINT FK_billing_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.users_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  username varchar NOT NULL,
  password varchar NOT NULL,
  enabled boolean NOT NULL,
  activation_token varchar,
  city varchar,
  first_name varchar,
  last_name varchar,
  birthday date,
  email varchar NOT NULL,
  language varchar,
  preferred_languages varchar,
  avatar varchar,
  views bigint NOT NULL,
  authority varchar NOT NULL,
  total_rating bigint,
  total_votes bigint,
  section_id bigint NOT NULL,
  comments_count bigint NOT NULL,
  online boolean NOT NULL,
  premium boolean NOT NULL,
  balance bigint NOT NULL,
  premium_expired timestamp,
  PRIMARY KEY (username, revision_id)
);

ALTER TABLE users_aud
  ADD CONSTRAINT FK_users_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.sections_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  name varchar,
  last_update date,
  description varchar,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE sections_aud
  ADD CONSTRAINT FK_sections_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.books_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  name varchar,
  user_id varchar NOT NULL,
  description varchar,
  created date,
  genre integer,
  serie_id bigint,
  last_update timestamp,
  language varchar,
  cover varchar,
  views bigint NOT NULL,
  comments_count bigint NOT NULL,
  total_rating bigint,
  total_votes bigint,
  review_count bigint,
  paid boolean NOT NULL,
  cost bigint,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE books_aud
  ADD CONSTRAINT FK_books_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.series_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  name varchar,
  user_id varchar NOT NULL,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE series_aud
  ADD CONSTRAINT FK_series_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.comments_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  book_id bigint NOT NULL,
  user_id varchar,
  comment varchar NOT NULL,
  created timestamp,
  related_to bigint NOT NULL,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE comments_aud
  ADD CONSTRAINT FK_comments_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.texts_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  text varchar,
  book_id bigint NOT NULL,
  prev_text varchar,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE texts_aud
  ADD CONSTRAINT FK_texts_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.chat_groups_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  creator_id varchar,
  created timestamp NOT NULL,
  primary_recipient varchar,
  name varchar,
  avatar varchar,
  unread_by_creator boolean NOT NULL,
  unread_by_recipient boolean NOT NULL,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE chat_groups_aud
  ADD CONSTRAINT FK_chat_groups_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.chat_groups_users_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  group_id bigint NOT NULL,
  user_id varchar NOT NULL,
  PRIMARY KEY (group_id, user_id, revision_id)
);

ALTER TABLE chat_groups_users_aud
  ADD CONSTRAINT FK_chat_groups_users_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.contests_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  creator varchar NOT NULL,
  prize_fund bigint NOT NULL,
  first_place_revenue integer NOT NULL,
  second_place_revenue integer NOT NULL,
  third_place_revenue integer NOT NULL,
  created timestamp NOT NULL,
  name varchar NOT NULL,
  started boolean NOT NULL,
  closed boolean NOT NULL,
  judge_count integer NOT NULL,
  participant_count integer NOT NULL,
  expiration_date timestamp,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE contests_aud
  ADD CONSTRAINT FK_contests_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.contest_judges_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  contest_id bigint NOT NULL,
  judge_id varchar NOT NULL,
  accepted boolean NOT NULL,
  PRIMARY KEY (contest_id, judge_id, revision_id)
);

ALTER TABLE contest_judges_aud
  ADD CONSTRAINT FK_contest_judges_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.contest_participants_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  contest_id bigint NOT NULL,
  participant_id varchar NOT NULL,
  accepted boolean NOT NULL,
  book_id bigint NOT NULL,
  PRIMARY KEY (contest_id, participant_id, book_id, revision_id)
);

ALTER TABLE contest_participants_aud
  ADD CONSTRAINT FK_contest_participants_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.friends_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  friend_id varchar NOT NULL,
  owner_id varchar NOT NULL,
  added timestamp NOT NULL,
  PRIMARY KEY (friend_id, owner_id, revision_id)
);

ALTER TABLE friends_aud
  ADD CONSTRAINT FK_friends_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.friendships_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  subscriber_id varchar NOT NULL,
  subscription_id varchar NOT NULL,
  date timestamp NOT NULL,
  active boolean NOT NULL,
  PRIMARY KEY (subscriber_id, subscription_id, revision_id)
);

ALTER TABLE friendships_aud
  ADD CONSTRAINT FK_friendships_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.gifts_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  gift_type integer NOT NULL,
  cost bigint,
  name varchar NOT NULL,
  description varchar,
  image varchar,
  category varchar,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE gifts_aud
  ADD CONSTRAINT FK_gifts_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.messages_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  creator_id varchar NOT NULL,
  message_text varchar NOT NULL,
  group_id bigint NOT NULL,
  created timestamp NOT NULL,
  unread boolean NOT NULL,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE messages_aud
  ADD CONSTRAINT FK_messages_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.news_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  news_type bigint NOT NULL,
  author_id varchar NOT NULL,
  book_id bigint,
  created timestamp NOT NULL,
  subscription_id varchar,
  contest_id bigint,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE news_aud
  ADD CONSTRAINT FK_news_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.ratings_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  book_id bigint NOT NULL,
  estimation integer NOT NULL,
  client_ip varchar NOT NULL,
  PRIMARY KEY (book_id, estimation, client_ip, revision_id)
);

ALTER TABLE ratings_aud
  ADD CONSTRAINT FK_ratings_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.reviews_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  book_id bigint NOT NULL,
  text varchar,
  author_id varchar NOT NULL,
  name varchar NOT NULL,
  score integer NOT NULL,
  likes bigint NOT NULL,
  dislikes bigint NOT NULL,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE reviews_aud
  ADD CONSTRAINT FK_reviews_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.reviews_ip_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  review_id bigint NOT NULL,
  ip varchar NOT NULL,
  PRIMARY KEY (review_id, ip, revision_id)
);

ALTER TABLE reviews_ip_aud
  ADD CONSTRAINT FK_reviews_ip_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.subscribers_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  subscriber_id varchar NOT NULL,
  owner_id varchar NOT NULL,
  added timestamp NOT NULL,
  PRIMARY KEY (subscriber_id, owner_id, revision_id)
);

ALTER TABLE subscribers_aud
  ADD CONSTRAINT FK_subscribers_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.subscriptions_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  subscription_id varchar NOT NULL,
  owner_id varchar NOT NULL,
  added timestamp NOT NULL,
  PRIMARY KEY (subscription_id, owner_id, revision_id)
);

ALTER TABLE subscriptions_aud
  ADD CONSTRAINT FK_subscriptions_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.user_book_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  user_id varchar NOT NULL,
  book_id bigint NOT NULL,
  PRIMARY KEY (user_id, book_id, revision_id)
);

ALTER TABLE user_book_aud
  ADD CONSTRAINT FK_user_book_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;

CREATE TABLE public.user_gift_aud (
  revision_id INTEGER NOT NULL,
  revision_type INT2,
  id bigint NOT NULL,
  gift_id bigint NOT NULL,
  user_id varchar NOT NULL,
  from_user varchar NOT NULL,
  message varchar,
  PRIMARY KEY (id, revision_id)
);

ALTER TABLE user_gift_aud
  ADD CONSTRAINT FK_user_gift_aud_audit
  FOREIGN KEY (revision_id)
  REFERENCES custom_audit_entity;