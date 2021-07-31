create table invitations
(
  id            varchar(100) primary key,
  code          varchar(250),
  status        varchar(100),
  addressed_to  varchar(250),
  email_address varchar(250),
  email_sent    boolean,
  created_at    timestamp with time zone,
  sent_at       timestamp with time zone,
  responded_at  timestamp with time zone
);

create table invitees
(
  id            varchar(100) primary key,
  invitation_id varchar(100),
  name          varchar(250),
  status        varchar(100),
  food_option   varchar(100),
  dietary_notes text
);
