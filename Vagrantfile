# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.

MACHINES = {

  "jenkins" => {
    ip: "192.168.56.10",
    ports: {
      forwarded: 8920,
      host: 8920
    }
  },
  "worker" => {
    ip: "192.168.56.11"
  },
  "main" => {
    ip: "192.168.56.12"
  }
}


FORWARDED_PORTS = {

  "kafka" => {
    host_port: 9092,
    forwarded_port: 9092
  },
  "mongo" => {
    host_port: 27017,
    forwarded_port: 27017
  },
  "mongo-express" => {
    host_port: 8820,
    forwarded_port: 8820
  },
  
}

$install_java = <<-'SCRIPT'

sudo apt update
sudo apt install -y fontconfig openjdk-17-jre

SCRIPT

$install_docker = <<-'SCRIPT'

if ! [ -x "$(command -v docker)" ]; then
  sudo apt-get update -y

  sudo apt-get update
  sudo apt-get install -y ca-certificates curl
  sudo install -m 0755 -d /etc/apt/keyrings
  sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
  sudo chmod a+r /etc/apt/keyrings/docker.asc

  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
    $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  sudo apt-get update

  sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

else
  echo "Docker is already installed. Version: $(docker --version)"
fi

SCRIPT





Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "bento/ubuntu-24.04"
  config.vm.box_version = "202502.21.0"


  config.vm.define "jenkins" do | jenkins |

    jenkins.vm.hostname = "jenkins"
    jenkins.vm.network "private_network", ip:MACHINES["jenkins"][:ip]

    jenkins.vm.network "forwarded_port", guest:MACHINES["jenkins"][:ports][:forwarded], host: MACHINES["jenkins"][:ports][:host]

    jenkins.vm.provider "virtualbox" do |vb|
      vb.memory = "1024"
    end


    # Provison to install java
    jenkins.vm.provision "shell", name:"setup-java", inline:$install_java

    jenkins.vm.provision "shell", name:"setup-jenkins", inline: <<-SHELL

      sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
      https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
      echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
      https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
      /etc/apt/sources.list.d/jenkins.list > /dev/null
      sudo apt-get update
      sudo apt-get install -y jenkins

    SHELL

  end
  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  config.vm.define "worker" do |worker|

    
    worker.vm.provision "shell", name: "setup-docker", inline:$install_docker
    worker.vm.provision "shell", name: "setup-java", inline:$install_java

    # Create a forwarded port mapping which allows access to a specific port
    # within the machine from a port on the host machine. In the example below,
    # accessing "localhost:8080" will access port 80 on the guest machine.
    # NOTE: This will enable public access to the opened port

    worker.vm.network "private_network", ip: MACHINES["worker"][:ip]
    
    # Iterate over the hash and perform an action
    FORWARDED_PORTS.each do |service_name, ports|
      puts "Configuring service: #{service_name}"
      puts "Host port: #{ports[:host_port]}, Forwarded port: #{ports[:forwarded_port]}"

      worker.vm.network "forwarded_port", guest: ports[:forwarded_port], host: ports[:host_port]
    end

    worker.vm.synced_folder "./", "/home/vagrant/project"

    worker.vm.provider "virtualbox" do |vb|
      vb.memory = "2048"
    end

  end


  # For the Continious deployment purpose activate this vm

  # config.vm.define "main" do | main |

  #   main.vm.hostname = "main"

  #   main.vm.network "private_network", ip:MACHINES["main"][:ip]

  #   main.vm.provider "virtualbox" do |vb|
  #     vb.memory = "2048"
  #   end
  # end

end
