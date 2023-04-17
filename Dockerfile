FROM bellsoft/liberica-openjdk-alpine:17
RUN mkdir /simpleblockchain
ADD . /simpleblockchain

ENV CURRENT_NODE_ADDRESS = currentNodeAddress \
    NODES_IN_CLUSTER_ADDRESSES = nodesInClusterAddresses \
    IS_GENESIS = isGenesis

RUN cd /simpleblockchain && ./gradlew jar