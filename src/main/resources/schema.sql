DROP TABLE IF EXISTS transferencias;
CREATE TABLE `transferencias` (
	  `id` bigint(20) NOT NULL,
	  `beneficiario` varchar(255) NOT NULL,
	  `fecha` date NOT NULL,
	  `importe` double NOT NULL,
	  `concepto` varchar(255) NOT NULL
) ;
ALTER TABLE `transferencias`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `transferencias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
