# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  # We start with a box preconfigured with docker. Docker is required
  # to build and deploy the Linkbait image to the container registry
  config.vm.box = "williamyeh/ubuntu-trusty64-docker"

  # IP for access to web interface
   config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.provider "virtualbox" do |vb|
    vb.memory = "512"
  end

  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update

    sudo apt-get install -y software-properties-common
    sudo add-apt-repository ppa:openjdk-r/ppa
    sudo apt-get update
    sudo apt-get install -y openjdk-8-jdk nodejs npm
    sudo apt-get install -y imagemagick
    sudo update-ca-certificates -f
  SHELL
end
