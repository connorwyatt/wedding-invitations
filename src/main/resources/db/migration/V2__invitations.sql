create table invitations (
  id varchar(100) primary key,
  code varchar(250),
  status varchar(100),
  invitationPosted boolean,
  emailAddress varchar(250),
  invitationEmailed boolean,
  createdAt timestamp with time zone,
  respondedAt timestamp with time zone
);

create table invitees (
  invitation_id varchar(100),
  name varchar(250),
  status varchar(100),
  food_option varchar(100),
  dietary_notes text
);
