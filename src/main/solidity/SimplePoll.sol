// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

contract SimplePoll {

    struct Voter {
        bool voted;
        bool canVote;
    }

    struct Option {
        bytes32 name;
        uint voteCount;
    }

    mapping(address => Voter) public voters;
    Option[] public options;

    address public initiator;

    constructor(bytes32[] memory optionNames, address[] memory allowedVoters) {
        initiator = msg.sender;

        for (uint i = 0; i < optionNames.length; i++) {
            options.push(Option({
            name: optionNames[i],
            voteCount: 0
            }));
        }

        for (uint i = 0; i < allowedVoters.length; i++) {
             voters[allowedVoters[i]].canVote = true;
        }
    }

    function vote(uint option) public {
        Voter storage sender = voters[msg.sender];
        require(sender.canVote, "Has no right to vote");
        require(!sender.voted, "Already voted.");
        sender.voted = true;

        options[option].voteCount ++;
    }

    function getOptionsCount() external view returns(uint) {
        return options.length;
    }

    function winningOption() public view returns (uint winningOption_) {
        uint winningVoteCount = 0;
        for (uint p = 0; p < options.length; p++) {
            if (options[p].voteCount > winningVoteCount) {
                winningVoteCount = options[p].voteCount;
                winningOption_ = p;
            }
        }
        return winningOption_;
    }

    function winnerName() public view returns (bytes32 winnerName_) {
        winnerName_ = options[winningOption()].name;
    }
}