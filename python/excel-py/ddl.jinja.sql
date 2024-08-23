CREATE TABLE "{{tb_space}}"."{{tb_name}}" (
    {% for col in tb_cols %}
    "{{col.name}}" {{col.type}} {{col.nullable}},
    {% endfor %}
    PRIMARY KEY ({{tb_pk}})
) WITH (
    OIDS = FALSE
)