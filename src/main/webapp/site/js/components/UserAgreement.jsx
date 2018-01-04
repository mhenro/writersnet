import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import {closeUserPolicy} from '../actions/GlobalActions.jsx';

class UserAgreement extends React.Component {
    onClose() {
        this.props.onCloseUserPolicy();
    }

    render() {
        return (
            <Modal bsSize="large" show={this.props.showUserPolicy} onHide={() => this.onClose()}>
                <Modal.Header>
                    User Agreement
                </Modal.Header>
                <Modal.Body>
                    <div>
                        <h3>1. PURPOSE</h3>
                        <br/><br/>
                        1.1. <b>WritersNets.com</b> provides all users with a free Internet platform for placing their electronic novels on the Internet, i.e. it performs the role of free electronic publisher for author novels.
                        <br/><br/>
                        1.2. Each registered user receives a personal page and an online personal account, in which he independently places his works.
                        <br/><br/>
                        1.3. You agree to abide by this <b>Agreement</b>, the actuality of which is certified by this document. All changes made to the document take effect 2 days after the publication of the new version. Users who are not aware of the existence of the <b>Agreement</b> are not exempt from liability for its non-fulfillment.
                        <br/><br/>
                        1.4. The <b>Agreement</b> is not subject to the establishment of agency relations between you and <b>WritersNets.com</b>, partnership relations, joint activity relations, personal hiring relations, or any other relations, and are not a contract of public offer. The <b>Agreement</b> regulates your activities on <b>WritersNets.com</b> to ensure your security and avoid deactivation of your resources.
                        <br/><br/>
                        1.5. <b>WritersNets.com</b> reserves the right to change the <b>Agreement</b> at any time without prior notice to users.
                        <br/><br/>
                        <h3>2. REGISTRATION AND SAFETY</h3>
                        <br/><br/>
                        2.1. Becoming a member of <b>WritersNets.com</b>, you receive registration information: login and password - to enter your personal account. We strongly recommend that you be attentive to your login and password, not to allow their loss and transfer to third parties.
                        <br/><br/>
                        2.2 You are fully responsible for the security of the registration data. Conversely, <b>WritersNets.com</b> is not responsible for the impossibility of your access to your own account in case of lost password. You agree that <b>WritersNets.com</b> has the right to refuse to authenticate you with the simultaneous absence of your email, login and password.
                        <br/><br/>
                        <h3>3. YOUR CONTENT</h3>
                        <br/><br/>
                        3.1. Our Service allow you to store and share <b>Your Content</b>. We don’t claim ownership of <b>Your Content</b>. <b>Your Content</b> remains your content and you are responsible for it.
                        <br/><br/>
                        3.2. You agree that you are responsible for all materials published in your personal account. On the contrary, <b>WritersNets.com</b> does not bear any responsibility for the materials published in your personal account. The opinion expressed on the pages and electronic novels of your personal account may not coincide with the opinion of <b>WritersNets.com</b>.
                        <br/><br/>
                        3.3. The personal accounts created in the <b>WritersNets.com</b> service are prohibited to post the following information:
                        <br/><br/>
                        3.3.1) propaganda of hatred and / or discrimination of people on racial, ethnic, sexual, religious and social grounds, as well as any other manifestation of intolerance to the opinions and way of life of people and their communities;
                        <br/><br/>
                        3.3.2) slander and / or insult to third parties and organizations, incl. use of obscene words and expressions to achieve this goal;
                        <br/><br/>
                        3.3.3) malicious programs designed to infringe, destroy or limit the functionality of any equipment or other programs, serial numbers to commercial software products and programs for their generation, means for obtaining unauthorized access to paid resources on the Internet, and links to such information;
                        <br/><br/>
                        3.3.4) objects of intellectual property, to which copyrights, copies of other people's works, texts, pages and separate elements from other projects placed on the Internet are copied, if copying is forbidden by the owners of the original (plagiarism);
                        <br/><br/>
                        3.3.5) materials that are contrary to the legislation of your country, incl. materials calling for unlawful activities, explaining the use of explosives, weapons, training in the production of malicious programs, etc;
                        <br/><br/>
                        3.3.6) spam and intrusive advertising in comments and discussions, if this information is not directly related to the topic of discussion. For example, advertising of other sites, products or services in discussion (comments) to works of authors.
                        <br/><br/>
                        3.4. You agree to respect the standards of network ethics in relation to the administration of <b>WritersNets.com</b>, other participants, authors and your readers. Proceeding from this provision, <b>WritersNets.com</b> forbids:
                        <br/><br/>
                        3.4.1) post content consisting entirely of links to other sites (link dumps) or spam advertising;
                        <br/><br/>
                        3.4.2) use the account and its functions solely to promote other sites: spam links, scoring visits, ratings and scores;
                        <br/><br/>
                        3.4.3) engage in trolling - social provocation or bullying in a network of communication aimed at other participants in the project.
                        <br/><br/>
                        3.4.4) register in the system many times, creating artificial or fake accounts for the purpose of cheating ratings or anti-ratings, cheating statistics of works, authors or other content, gaining access to content bypassing the ban or for other purposes.
                        <br/><br/>
                        <h3>4. RESOURCES.</h3>
                        4.1. You agree to use the services of <b>WritersNets.com</b> in the form in which they exist ("as is"). <b>WritersNets.com</b> does not upgrade individual services and does not develop new services in accordance with your personal needs.
                        <br/><br/>
                        4.2. <b>WritersNets.com</b> does not bear any responsibility for the loss of data in users' accounts, any errors and malfunctions in the system operation and possible delays in data transmission. <b>WritersNets.com</b> does not provide any guarantees of the operability of the services. You agree to use the services of <b>WritersNets.com</b> "at your own peril and risk".
                        <br/><br/>
                        4.3. You realize that the resources of <b>WritersNets.com</b>, for all its significance, are not unlimited, and providing them in mass order requires the introduction of standard restrictions on their use. Limitations established for various services may change over time, depending on the number of participants and the resource capacity of the project.
                        <br/><br/>
                        4.4. <b>WritersNets.com</b> can stop the work of any of its functions by prior notification of users.
                        <br/><br/>
                        4.5. <b>WritersNets.com</b> can simultaneously stop all functions for general prevention and service updates. This can occur both on prior notification to users, and without prior notice.
                        <br/><br/>
                        4.6. <b>WritersNets.com</b> has the right to block access to the project's functions to any user in case of violation of this <b>Agreement</b>, and also, in case of receipt of grounded complaints from other Internet visitors.
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showUserPolicy: state.GlobalReducer.showUserPolicy
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onCloseUserPolicy: () => {
            dispatch(closeUserPolicy());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(UserAgreement);