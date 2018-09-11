CREATE TABLE public.billing (
    id bigint NOT NULL,
    user_id varchar NOT NULL,
    operation_type integer,
    operation_cost bigint NOT NULL DEFAULT 0,
    balance bigint NOT NULL DEFAULT 0,
    operation_date timestamp,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.billing
    (user_id);


CREATE TABLE public.users (
    username varchar NOT NULL,
    password varchar NOT NULL,
    enabled boolean NOT NULL DEFAULT true,
    activation_token varchar,
    city varchar,
    first_name varchar,
    last_name varchar,
    birthday date,
    email varchar NOT NULL,
    language varchar,
    preferred_languages varchar,
    avatar varchar,
    views bigint NOT NULL DEFAULT 0,
    authority varchar NOT NULL,
    total_rating bigint DEFAULT 0,
    total_votes bigint DEFAULT 0,
    section_id bigint NOT NULL,
    comments_count bigint NOT NULL DEFAULT 0,
    online boolean NOT NULL DEFAULT false,
    premium boolean NOT NULL DEFAULT false,
    balance bigint NOT NULL DEFAULT 0,
    premium_expired timestamp,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (username)
);

ALTER TABLE public.users
    ADD UNIQUE (email);

CREATE INDEX ON public.users
    (section_id);


CREATE TABLE public.sections (
    id bigint NOT NULL,
    name varchar,
    last_update date,
    description varchar,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);


CREATE TABLE public.books (
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
    views bigint NOT NULL DEFAULT 0,
    comments_count bigint NOT NULL DEFAULT 0,
    total_rating bigint DEFAULT 0,
    total_votes bigint DEFAULT 0,
    review_count bigint DEFAULT 0,
    paid boolean NOT NULL,
    cost bigint,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.books
    (user_id);
CREATE INDEX ON public.books
    (serie_id);


CREATE TABLE public.series (
    id bigint NOT NULL,
    name varchar,
    user_id varchar NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.series
    (user_id);


CREATE TABLE public.comments (
    id bigint NOT NULL,
    book_id bigint NOT NULL,
    user_id varchar,
    comment varchar NOT NULL,
    created timestamp,
    related_to bigint,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.comments
    (book_id);
CREATE INDEX ON public.comments
    (user_id);
CREATE INDEX ON public.comments
    (related_to);


CREATE TABLE public.texts (
    id bigint NOT NULL,
    text varchar,
    book_id bigint NOT NULL,
    prev_text varchar,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.texts
    (book_id);


CREATE TABLE public.captcha (
    id bigint NOT NULL,
    code varchar NOT NULL,
    expired timestamp NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE public.captcha
    ADD UNIQUE (code);


CREATE TABLE public.chat_groups (
    id bigint NOT NULL,
    creator_id varchar,
    created timestamp NOT NULL,
    primary_recipient varchar,
    name varchar,
    avatar varchar,
    unread_by_creator boolean NOT NULL DEFAULT false,
    unread_by_recipient boolean NOT NULL DEFAULT false,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.chat_groups
    (creator_id);
CREATE INDEX ON public.chat_groups
    (primary_recipient);


CREATE TABLE public.chat_groups_users (
    group_id bigint NOT NULL,
    user_id varchar NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (group_id, user_id)
);


CREATE TABLE public.contests (
    id bigint NOT NULL,
    creator varchar NOT NULL,
    prize_fund bigint NOT NULL DEFAULT 0,
    first_place_revenue integer NOT NULL DEFAULT 100,
    second_place_revenue integer NOT NULL DEFAULT 0,
    third_place_revenue integer NOT NULL DEFAULT 0,
    created timestamp NOT NULL,
    name varchar NOT NULL,
    started boolean NOT NULL DEFAULT false,
    closed boolean NOT NULL DEFAULT false,
    judge_count integer NOT NULL DEFAULT 0,
    participant_count integer NOT NULL DEFAULT 0,
    expiration_date timestamp,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.contests
    (creator);


CREATE TABLE public.contest_judges (
    contest_id bigint NOT NULL,
    judge_id varchar NOT NULL,
    accepted boolean NOT NULL DEFAULT false,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (contest_id, judge_id)
);


CREATE TABLE public.contest_participants (
    contest_id bigint NOT NULL,
    participant_id varchar NOT NULL,
    accepted boolean NOT NULL DEFAULT false,
    book_id bigint NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (contest_id, participant_id, book_id)
);


CREATE TABLE public.friends (
    friend_id varchar NOT NULL,
    owner_id varchar NOT NULL,
    added timestamp NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (friend_id, owner_id)
);


CREATE TABLE public.friendships (
    subscriber_id varchar NOT NULL,
    subscription_id varchar NOT NULL,
    date timestamp NOT NULL,
    active boolean NOT NULL DEFAULT false,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (subscriber_id, subscription_id)
);


CREATE TABLE public.gifts (
    id bigint NOT NULL,
    gift_type integer NOT NULL,
    cost bigint,
    name varchar NOT NULL,
    description varchar,
    image varchar,
    category varchar,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);


CREATE TABLE public.messages (
    id bigint NOT NULL,
    creator_id varchar NOT NULL,
    message_text varchar NOT NULL,
    group_id bigint NOT NULL,
    created timestamp NOT NULL,
    unread boolean NOT NULL DEFAULT false,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.messages
    (creator_id);
CREATE INDEX ON public.messages
    (group_id);


CREATE TABLE public.news (
    id bigint NOT NULL,
    news_type bigint NOT NULL,
    author_id varchar NOT NULL,
    book_id bigint,
    created timestamp NOT NULL,
    subscription_id varchar,
    contest_id bigint,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.news
    (author_id);
CREATE INDEX ON public.news
    (book_id);
CREATE INDEX ON public.news
    (subscription_id);
CREATE INDEX ON public.news
    (contest_id);


CREATE TABLE public.ratings (
    book_id bigint NOT NULL,
    estimation integer NOT NULL,
    client_ip varchar NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (book_id, estimation, client_ip)
);


CREATE TABLE public.reviews (
    id bigint NOT NULL,
    book_id bigint NOT NULL,
    text varchar,
    author_id varchar NOT NULL,
    name varchar NOT NULL,
    score integer NOT NULL DEFAULT 0,
    likes bigint NOT NULL DEFAULT 0,
    dislikes bigint NOT NULL DEFAULT 0,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.reviews
    (book_id);
CREATE INDEX ON public.reviews
    (author_id);


CREATE TABLE public.reviews_ip (
    review_id bigint NOT NULL,
    ip varchar NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (review_id, ip)
);


CREATE TABLE public.sessions (
    id bigint NOT NULL,
    username varchar NOT NULL,
    expire_date timestamp NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE public.sessions
    ADD UNIQUE (username);


CREATE TABLE public.subscribers (
    subscriber_id varchar NOT NULL,
    owner_id varchar NOT NULL,
    added timestamp NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (subscriber_id, owner_id)
);


CREATE TABLE public.subscriptions (
    subscription_id varchar NOT NULL,
    owner_id varchar NOT NULL,
    added timestamp NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (subscription_id, owner_id)
);


CREATE TABLE public.user_book (
    user_id varchar NOT NULL,
    book_id bigint NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, book_id)
);


CREATE TABLE public.user_gift (
    id bigint NOT NULL,
    gift_id bigint NOT NULL,
    user_id varchar NOT NULL,
    from_user varchar NOT NULL,
    message varchar,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE INDEX ON public.user_gift
    (gift_id);
CREATE INDEX ON public.user_gift
    (user_id);
CREATE INDEX ON public.user_gift
    (from_user);


CREATE TABLE public.contest_rating (
    contest_id bigint NOT NULL,
    judge_id varchar NOT NULL,
    book_id bigint NOT NULL,
    estimation smallint NOT NULL,
    opt_lock bigint NOT NULL DEFAULT 0,
    PRIMARY KEY (contest_id, judge_id, book_id)
);


ALTER TABLE public.billing ADD CONSTRAINT FK_billing__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.users ADD CONSTRAINT FK_users__section_id FOREIGN KEY (section_id) REFERENCES public.sections(id);
ALTER TABLE public.books ADD CONSTRAINT FK_books__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.books ADD CONSTRAINT FK_books__serie_id FOREIGN KEY (serie_id) REFERENCES public.series(id);
ALTER TABLE public.series ADD CONSTRAINT FK_series__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.comments ADD CONSTRAINT FK_comments__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.comments ADD CONSTRAINT FK_comments__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.comments ADD CONSTRAINT FK_comments__related_to FOREIGN KEY (related_to) REFERENCES public.comments(id);
ALTER TABLE public.texts ADD CONSTRAINT FK_texts__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.chat_groups ADD CONSTRAINT FK_chat_groups__creator_id FOREIGN KEY (creator_id) REFERENCES public.users(username);
ALTER TABLE public.chat_groups ADD CONSTRAINT FK_chat_groups__primary_recipient FOREIGN KEY (primary_recipient) REFERENCES public.users(username);
ALTER TABLE public.chat_groups_users ADD CONSTRAINT FK_chat_groups_users__group_id FOREIGN KEY (group_id) REFERENCES public.chat_groups(id);
ALTER TABLE public.chat_groups_users ADD CONSTRAINT FK_chat_groups_users__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.contests ADD CONSTRAINT FK_contests__creator FOREIGN KEY (creator) REFERENCES public.users(username);
ALTER TABLE public.contest_judges ADD CONSTRAINT FK_contest_judges__contest_id FOREIGN KEY (contest_id) REFERENCES public.contests(id);
ALTER TABLE public.contest_judges ADD CONSTRAINT FK_contest_judges__judge_id FOREIGN KEY (judge_id) REFERENCES public.users(username);
ALTER TABLE public.contest_participants ADD CONSTRAINT FK_contest_participants__contest_id FOREIGN KEY (contest_id) REFERENCES public.contests(id);
ALTER TABLE public.contest_participants ADD CONSTRAINT FK_contest_participants__participant_id FOREIGN KEY (participant_id) REFERENCES public.users(username);
ALTER TABLE public.contest_participants ADD CONSTRAINT FK_contest_participants__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.friends ADD CONSTRAINT FK_friends__friend_id FOREIGN KEY (friend_id) REFERENCES public.users(username);
ALTER TABLE public.friends ADD CONSTRAINT FK_friends__owner_id FOREIGN KEY (owner_id) REFERENCES public.users(username);
ALTER TABLE public.friendships ADD CONSTRAINT FK_friendships__subscriber_id FOREIGN KEY (subscriber_id) REFERENCES public.users(username);
ALTER TABLE public.friendships ADD CONSTRAINT FK_friendships__subscription_id FOREIGN KEY (subscription_id) REFERENCES public.users(username);
ALTER TABLE public.messages ADD CONSTRAINT FK_messages__creator_id FOREIGN KEY (creator_id) REFERENCES public.users(username);
ALTER TABLE public.messages ADD CONSTRAINT FK_messages__group_id FOREIGN KEY (group_id) REFERENCES public.chat_groups(id);
ALTER TABLE public.news ADD CONSTRAINT FK_news__author_id FOREIGN KEY (author_id) REFERENCES public.users(username);
ALTER TABLE public.news ADD CONSTRAINT FK_news__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.news ADD CONSTRAINT FK_news__subscription_id FOREIGN KEY (subscription_id) REFERENCES public.users(username);
ALTER TABLE public.news ADD CONSTRAINT FK_news__contest_id FOREIGN KEY (contest_id) REFERENCES public.contests(id);
ALTER TABLE public.ratings ADD CONSTRAINT FK_ratings__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.reviews ADD CONSTRAINT FK_reviews__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.reviews ADD CONSTRAINT FK_reviews__author_id FOREIGN KEY (author_id) REFERENCES public.users(username);
ALTER TABLE public.sessions ADD CONSTRAINT FK_sessions__username FOREIGN KEY (username) REFERENCES public.users(username);
ALTER TABLE public.subscribers ADD CONSTRAINT FK_subscribers__subscriber_id FOREIGN KEY (subscriber_id) REFERENCES public.users(username);
ALTER TABLE public.subscribers ADD CONSTRAINT FK_subscribers__owner_id FOREIGN KEY (owner_id) REFERENCES public.users(username);
ALTER TABLE public.subscriptions ADD CONSTRAINT FK_subscriptions__subscription_id FOREIGN KEY (subscription_id) REFERENCES public.users(username);
ALTER TABLE public.subscriptions ADD CONSTRAINT FK_subscriptions__owner_id FOREIGN KEY (owner_id) REFERENCES public.users(username);
ALTER TABLE public.user_book ADD CONSTRAINT FK_user_book__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.user_book ADD CONSTRAINT FK_user_book__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);
ALTER TABLE public.user_gift ADD CONSTRAINT FK_user_gift__gift_id FOREIGN KEY (gift_id) REFERENCES public.gifts(id);
ALTER TABLE public.user_gift ADD CONSTRAINT FK_user_gift__user_id FOREIGN KEY (user_id) REFERENCES public.users(username);
ALTER TABLE public.user_gift ADD CONSTRAINT FK_user_gift__from_user FOREIGN KEY (from_user) REFERENCES public.users(username);
ALTER TABLE public.contest_rating ADD CONSTRAINT FK_contest_rating__contest_id FOREIGN KEY (contest_id) REFERENCES public.contests(id);
ALTER TABLE public.contest_rating ADD CONSTRAINT FK_contest_rating__judge_id FOREIGN KEY (judge_id) REFERENCES public.users(username);
ALTER TABLE public.contest_rating ADD CONSTRAINT FK_contest_rating__book_id FOREIGN KEY (book_id) REFERENCES public.books(id);