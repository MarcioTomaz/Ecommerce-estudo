INSERT INTO _currency (active, creation_date, code, name, symbol)
SELECT 1, CURRENT_DATE, 'BRL', 'Real', 'R$'
    WHERE NOT EXISTS (SELECT 1 FROM _currency WHERE code = 'BRL');

INSERT INTO _currency (active, creation_date, code, name, symbol)
SELECT 1, CURRENT_DATE, 'USD', 'Dólar', '$'
    WHERE NOT EXISTS (SELECT 1 FROM _currency WHERE code = 'USD');

INSERT INTO _currency (active, creation_date, code, name, symbol)
SELECT 1, CURRENT_DATE, 'GBP', 'Libra', '£'
    WHERE NOT EXISTS (SELECT 1 FROM _currency WHERE code = 'GBP');
