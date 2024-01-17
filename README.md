# PEP1 Métodos de Ingeniería de Software
Aplicación web con un estilo de arquitectura monolítico desarrollada como proyecto para la asignatura Métodos de Ingeniería de Software de la USACH.

El foco de este proyecto está en implementar las distintas funcionalidades, cumplir con las reglas de negocio, realizar tests unitarios, utilizar una arquitectura monolítica, contenerizar las aplicaciones, desplegar en la nube, automatizar el despliegue e implementar un pipeline de integración continua.

Las funcionalidades están dadas por un enunciado que nos sitúa en el contexto de un preuniversitario que necesita gestionar los pagos de arancel de sus estudiantes (ver "enunciado.pdf"), las historias de usuario que han sido implementadas son las siguientes:
 
- HU1: Ingreso de datos de los estudiantes desde pantalla.
- HU2: Generar cuotas de pago.
- HU3: Listar cuotas de pago de un estudiante y el estado de pago de cada cuota. 
- HU4: Registrar pagos de cuotas de arancel. 
- HU5: Importar notas de exámenes desde archivo Excel. 
- HU6: Calcular planilla de pagos de arancel 
- HU7: Calcular reporte resumen de estado de pagos de los estudiantes.

## Tecnologias relevantes

- Docker y docker-compose para correr dentro de contenedores las distintas aplicaciones.
- Spring Boot del lado del servidor cuya imagen se crea a patir del Dockerfile en /topeducation/Dockerfile, se levantan 3 contenedores como réplicas con el archivo de docker compose que se encuentra en /deployment-files/ansible/te-server/docker-compose.yml.
- Thymeleaf para generar las distintas vistas de la UI, los templates se encuentran en /topeducation/src/main/resources/templates.
- JUnit y Mockito para los tests unitarios de los servicios, que se encuentran en /topeducation/src/test/java/com/mingeso/topeducation.
- MySQL como base de datos, se levanta en un contenedor a patir del archivo de docker compose que se encuentra en /deployment-files/ansible/te-server con un volumen que se encuentra en /deployment-files/ansible/te-server/data/db. 
- Nginx como balanceador de carga, su archivo de configuracion se encuentra en /deployment-files/ansible/te-server/nginx/conf.d/topeducation.conf, y se levanta su contenedor con el archivo de docker compose que se encuentra en /deployment-files/ansible/te-server/docker-compose.yml.
- Jenkins para crear el pipeline de integración continua que es ejecutado cada vez que se hace un commit al repositorio de Github gracias a un Webhook, es levantado en un contenedor a partir del archivo de docker compose que se encuentra en /deployment-files/ansible/jenkins-server/docker-compose.yml, su configuración fue realizada de manera local y está almacenada en el volumen que se encuentra en /deployment-files/ansible/jenkins-server/jenkins_home, el Jenkinsfile con el pipeline se encuentra en /topeducation/Jenkinsfile. 
- Webhook (https://github.com/adnanh/webhook) para crear un endpoint que reciba notificaciones cuando la imagen de Docker de la aplicación sea actualizada y ejecute un script para que haga pull de la imagen nueva. Su archivo de configuración se encuentra en /deployment-files/ansible/te-server/webhook.conf y el script que actualiza la imagen de la aplicación y su respectivo contenedor se encuentra en /deployment-files/ansible/te-server/redeploy.sh.
- Terraform para levantar las máquinas virtuales y configurar sus conexiones de red en Azure, se puede rervisar el script en /topeducation/deployment-files/terraform.
- Ansible para provisionar a cada máquina virtual con sus respectivas aplicaciones, se pueden revisar todos los scripts en /topeducation/deployment-files/ansible (uno por servidor/VM). Cabe destacar que a partir del output del script de Terraform se obtienen las llaves para acceder por SSH a los servidores creados, estas llaves deben situarse en las carpetas de Ansible solo con permisos de lectura para que Ansible pueda conectarse sin problemas, además se deben cambiar las IP's de los inventory.ini con las IP's de las VM creadas.


## Diagrama Integración Continua

Al realizar un cambio en la aplicación en local y un commit en el repositorio, los cambios se reflejan automáticamente en la aplicación desplegada en la nube, esto gracias a que se utilizan distintas herramientas para lograr una integración continua. En primera instancia, se especifica un Webhook en el repositorio de Github que hace una notificación por medio de una petición HTTP de tipo POST cada vez que se realiza un commit en el repositorio hacia el hook especificado, que en este caso es el endpoint que provee el plugin de Git en Jenkins http://ip-server-jenkins:8080/github-webhook/, cuando el hook del servidor de Jenkins recibe la notificación, ejecuta el pipeline que está configurado (Ver Jenkinsfile), haciendo los tests unitarios, el build de la aplicación, el build de la imagen de Docker y el push de la imagen de Docker en Docker Hub (si se pasan correctamente todas las etapas del pipeline). Por su parte, en Docker Hub también se configura un Webhook ligado al repositorio de la imagen de la aplicación, el cual está en la ruta http://ip-server-aplicacion:9000/hooks/redeploy-webhook y también recibe una notificación cada vez que el repositorio (Docker Hub) es modificado. Finalmente, cuando el hook, creado gracias a la herramienta Webhook, recibe la petición HTTP, ejecuta el script redeploy.sh que básicamente realiza nuevamente un pull de la imagen, obteniendo así la versión actualizada, y levanta nuevamente los contenedores haciendo efectivos los cambios en la aplicación en producción.
  

![alt text](https://github.com/hgallardoaraya/pep1-mingeso/blob/main/cicdte.jpeg)


