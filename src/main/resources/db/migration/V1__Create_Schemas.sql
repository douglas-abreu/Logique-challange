create schema if not exists tipos;
create schema if not exists entidades;

create table if not exists tipos.permissao (
    id serial primary key,
    nome varchar not null
);

create table if not exists entidades.usuario (
    id serial primary key,
    permissao_id integer references tipos.permissao not null,
    usuario varchar not null,
    senha varchar not null,
    jornada_trabalho varchar
);

create table if not exists entidades.marcacao (
    id serial primary key,
    usuario_id integer references entidades.usuario not null,
    data_marcacao timestamp,
    marcacao_abertura timestamp,
    marcacao_fechamento timestamp
);

insert into tipos.permissao (id, nome)
values (1, 'administrador'),
       (2, 'usuario')
    ON CONFLICT DO NOTHING;

INSERT INTO entidades.usuario(usuario, senha, permissao_id)
values ('admin', '$2a$10$na8krNR55AUTpE1cW6/Qm.zTaxyhNWCOvqjgwZ/1VNF7se6I.RyIe', 1)
    ON CONFLICT DO NOTHING;


INSERT INTO entidades.usuario(usuario, senha, permissao_id, jornada_trabalho)
values ('jornada8', '$2a$10$046jhxkkxzJfaTquO28q5eGZkIenrd6kSdcrFYu7u.stWJz4TFmXy', 2, '08:00:00'),
        ('jornada6', '$2a$10$046jhxkkxzJfaTquO28q5eGZkIenrd6kSdcrFYu7u.stWJz4TFmXy', 2, '06:00:00')
    ON CONFLICT DO NOTHING;
